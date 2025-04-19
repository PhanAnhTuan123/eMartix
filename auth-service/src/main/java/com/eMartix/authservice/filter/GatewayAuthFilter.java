package com.eMartix.authservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class GatewayAuthFilter extends OncePerRequestFilter {

    @Value("${gateway.x-api-key}")
    private String X_API_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Kiểm tra API key trong header
        String apiKey = request.getHeader("X-API-KEY");
        log.info("Validating API Key: {}", apiKey);
        log.info("Expected API Key: {}", X_API_KEY);
        // Kiểm tra xem API key có hợp lệ không
        log.info("isValidApiKey: X_API_KEY = {}, apiKey = {}, {}", X_API_KEY, apiKey, X_API_KEY.equals(apiKey));

        if (!isValidApiKey(apiKey)) {
            throw new ServletException("Invalid API Key");
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidApiKey(String apiKey) {
        return X_API_KEY.equals(apiKey);
    }
}
