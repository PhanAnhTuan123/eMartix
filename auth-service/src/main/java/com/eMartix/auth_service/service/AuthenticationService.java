package com.eMartix.auth_service.service;

import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.request.RegisterRequestDto;
import com.eMartix.auth_service.dto.response.LoginResponse;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    LoginResponse authenticateUser(LoginRequestDto request, HttpServletResponse response);

    LoginResponse createRefreshToken(String username);

//    String logout(HttpServletRequest request);

    UserResponseDto register(RegisterRequestDto request);
}
