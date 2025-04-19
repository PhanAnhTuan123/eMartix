package com.eMartix.gatewayservice.filter;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${gateway.x-api-key}")
    private String X_API_KEY;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Lấy đường dẫn của request
        String path = exchange.getRequest().getPath().toString();

        if (isAuthRequest(exchange)) {
            return chain.filter(exchange);
        }
        String token = extractJwtFromRequest(exchange);
        if (token == null || !isValidToken(token)) {
            return Mono.error(new JwtException("Invalid or missing JWT token"));
        }
        Claims claims = extractClaims(token);
        List<SimpleGrantedAuthority> authorities = extractAuthoritiesFromClaims(claims);
        Authentication authentication = createAuthentication(claims, authorities);
        return setSecurityContextAndContinue(exchange, chain, authentication, token);
    }

    private boolean isAuthRequest(ServerWebExchange exchange) {
        String uri = exchange.getRequest().getURI().toString();
        return uri.contains("/api/v1/auth/login") || uri.contains("/api/v1/auth/register") || uri.contains("/api/v1/auth/refresh") || uri.contains("/api/v1/auth/send-verification-otp") ;
    }

    private String extractJwtFromRequest(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        return bearerToken != null && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }

    private boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws =  Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            Claims claims = claimsJws.getPayload();
            // Kiểm tra thời gian hết hạn
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                log.error("JWT token has expired");
                return false;
            }
            return true;
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaims(Claims claims) {
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List) {
            return ((List<?>) rolesObject).stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> ((Map<?, ?>) item).get("authority"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            log.warn("Roles are not in the expected format");
            return Collections.emptyList();
        }
    }

    private Authentication createAuthentication(Claims claims, List<SimpleGrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    private Mono<Void> setSecurityContextAndContinue(ServerWebExchange exchange, WebFilterChain chain, Authentication authentication, String token) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        exchange.getRequest().mutate()
                .header("Authorization", "Bearer " + token)
                .header("X-API-KEY", X_API_KEY );
        return exchange.getSession()
                .flatMap(session -> {
                    session.getAttributes().put("SPRING_SECURITY_CONTEXT", securityContext);
                    return chain.filter(exchange);
                });
    }


}
