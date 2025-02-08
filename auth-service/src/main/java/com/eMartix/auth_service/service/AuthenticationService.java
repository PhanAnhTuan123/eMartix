package com.eMartix.auth_service.service;

import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.response.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    TokenResponseDto createAccessToken(LoginRequestDto request);

    TokenResponseDto createRefreshToken(HttpServletRequest request);

    String logout(HttpServletRequest request);
}
