package com.eMartix.auth_service.service;


import com.eMartix.auth_service.common.TokenType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface JwtService {

    String generateAccessToken(String userId, String username, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshToken(String userId, String username, Collection<? extends GrantedAuthority> authorities);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType tokenType, UserDetails user);

    boolean validateToken(String token, TokenType tokenType);
}
