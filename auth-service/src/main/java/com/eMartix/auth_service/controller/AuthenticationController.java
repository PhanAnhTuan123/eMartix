package com.eMartix.auth_service.controller;


import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.request.RegisterRequestDto;
import com.eMartix.auth_service.dto.response.LoginResponse;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import com.eMartix.auth_service.service.AuthenticationService;
import com.eMartix.auth_service.service.TokenService;
import com.eMartix.commons.dtos.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
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
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request) {
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

        return new ResponseEntity<>(authenticationService.createRefreshToken(refreshToken), HttpStatus.OK);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        log.info("POST /logout");
//        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
//    }

}
