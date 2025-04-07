package com.eMartix.auth_service.configuration;

import com.eMartix.auth_service.filter.JwtAuthenticationFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain configure(@NonNull HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)) // Xử lý lỗi Unauthorized
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/auth/login/**", "/api/v1/auth/register", "/api/v1/auth/refresh").permitAll()
                        .anyRequest().authenticated()  // Các API khác yêu cầu auth
                )
                .sessionManagement(manager -> manager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Add our custom JWT security filter
        return http.build();
    }


}