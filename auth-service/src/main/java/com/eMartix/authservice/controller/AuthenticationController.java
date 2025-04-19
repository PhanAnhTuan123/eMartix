package com.eMartix.authservice.controller;

import com.eMartix.authservice.dto.request.LoginRequestDto;
import com.eMartix.authservice.dto.request.RegisterRequestDto;
import com.eMartix.authservice.dto.request.VerifyOtpRequestDto;
import com.eMartix.authservice.dto.response.LoginResponse;
import com.eMartix.authservice.dto.response.UserResponseDto;
import com.eMartix.authservice.service.AuthenticationService;
import com.eMartix.authservice.service.EmailService;
import com.eMartix.commons.dtos.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authenticationService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        UserResponseDto user = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully", user));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Refreshing token");
        // Lấy tất cả cookies từ request
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Kiểm tra xem có cookie nào tên là "refreshToken"
                if ("refreshToken".equals(cookie.getName())) {
                    // Trả về giá trị của refreshToken
                    refreshToken = cookie.getValue();
                }
            }
        }

        return new ResponseEntity<>(authenticationService.createRefreshToken(refreshToken, response), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /logout");

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse(true, authenticationService.logout(request, response)));
    }

    @PostMapping("send-verification-otp")
    public ResponseEntity<ApiResponse> sendVerificationEmail(@RequestBody VerifyOtpRequestDto requestDto) {
        boolean rs =authenticationService.verifyEmail(requestDto);
        return ResponseEntity.ok(new ApiResponse(rs, "Verification email sent successfully"));
    }

    // forgot password
//    @PostMapping("send-reset-password-otp")
//    public ResponseEntity<ApiResponse> sendResetPasswordEmail(@RequestBody() String email) {
//
//
//        return ResponseEntity.ok(new ApiResponse(, "Reset password email sent successfully"));
//    }
}
