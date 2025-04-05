package com.eMartix.auth_service.controller;


import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.request.RegisterRequestDto;
import com.eMartix.auth_service.dto.response.LoginResponse;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import com.eMartix.auth_service.service.AuthenticationService;
import com.eMartix.commons.dtos.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponse loginResponse = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        UserResponseDto user = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully", user));
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request) {
//        log.info("POST /refresh-token");
//        return new ResponseEntity<>(authenticationService.createRefreshToken(request), HttpStatus.OK);
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        log.info("POST /logout");
//        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
//    }

}
