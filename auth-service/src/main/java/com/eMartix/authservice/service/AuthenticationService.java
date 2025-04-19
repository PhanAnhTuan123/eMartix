package com.eMartix.authservice.service;

import com.eMartix.authservice.dto.request.LoginRequestDto;
import com.eMartix.authservice.dto.request.RegisterRequestDto;
import com.eMartix.authservice.dto.request.VerifyOtpRequestDto;
import com.eMartix.authservice.dto.response.LoginResponse;
import com.eMartix.authservice.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    LoginResponse authenticateUser(LoginRequestDto request, HttpServletResponse response);

    LoginResponse createRefreshToken(String username,HttpServletResponse response);

    String logout(HttpServletRequest request, HttpServletResponse response);

    UserResponseDto register(RegisterRequestDto request);

    boolean verifyEmail(VerifyOtpRequestDto requestDto);

    void sentRequireForgotPassword(String email);
}
