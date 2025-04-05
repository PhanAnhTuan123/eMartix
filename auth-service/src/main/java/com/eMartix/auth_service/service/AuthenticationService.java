package com.eMartix.auth_service.service;

import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.request.RegisterRequestDto;
import com.eMartix.auth_service.dto.response.LoginResponse;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    LoginResponse authenticateUser(LoginRequestDto request);

//    LoginResponse createRefreshToken(HttpServletRequest request);

//    String logout(HttpServletRequest request);

    UserResponseDto register(RegisterRequestDto request);
}
