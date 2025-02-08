package com.eMartix.auth_service.controller;


import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.response.TokenResponseDto;
import com.eMartix.auth_service.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> accessToken(@RequestBody LoginRequestDto request) {
        log.info("POST /access-token");
        return new ResponseEntity<>(authenticationService.createAccessToken(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(HttpServletRequest request) {
        log.info("POST /refresh-token");
        return new ResponseEntity<>(authenticationService.createRefreshToken(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.info("POST /logout");
        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
    }

}
