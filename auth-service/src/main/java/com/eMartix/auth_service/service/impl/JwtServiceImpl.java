package com.eMartix.auth_service.service.impl;


import com.eMartix.auth_service.common.TokenType;
import com.eMartix.auth_service.exception.InvalidDataException;
import com.eMartix.auth_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.eMartix.auth_service.common.TokenType.ACCESS_TOKEN;
import static com.eMartix.auth_service.common.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private long expiryHour;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String userId, String username, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateToken(claims, username);
    }

    @Override
    public String generateRefreshToken(String userId, String username, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);

        return generateRefreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private String generateToken(Map<String, Object> claims, String username) {
        log.info("---------- generateToken ----------");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryHour)) // 1 ngày
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("---------- generateRefreshToken ----------");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        log.info("---------- getKey ----------");
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }

    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     * @param token
     * @return all claims
     */
    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parser()
                .setSigningKey(getKey(type)).build() // lấy ra secret key để decode token
                .parseClaimsJws(token) // parse token
                .getBody(); // lấy ra body của token
    }

    /**
     * Kiểm tra token có hợp lệ không
     * @param token
     * @param type
     * @return true nếu token hợp lệ
     */
    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    /**
     * Kiểm tra token có hợp lệ không
     * @param token
     * @param tokenType
     * @param user
     * @return
     */
    @Override
    public boolean isValid(String token, TokenType tokenType, UserDetails user) {
        final String username = extractUsername(token, tokenType);
        return (username.equals(user.getUsername()) && !isTokenExpired(token, tokenType));
    }

    /**
     * JWT được cấu thafnh từ 3 phần la header, payload và signature
     * Header: chứa thông tin về loại token và thuật toán mã hóa
     * Payload: chứa thông tin mà bạn muốn lưu trong token
     * Signature: chứa chữ ký của token
     */
}
