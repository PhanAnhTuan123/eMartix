package com.eMartix.auth_service.service.impl;

import com.eMartix.auth_service.common.TokenType;
import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.response.TokenResponseDto;
import com.eMartix.auth_service.exception.InvalidDataException;
import com.eMartix.auth_service.model.Token;
import com.eMartix.auth_service.model.User;
import com.eMartix.auth_service.repository.TokenRepository;
import com.eMartix.auth_service.repository.UserRepository;
import com.eMartix.auth_service.service.AuthenticationService;
import com.eMartix.auth_service.service.JwtService;
import com.eMartix.auth_service.service.TokenService;
import com.eMartix.commons.advice.ResourceNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;

    //login
    @Override
    public TokenResponseDto createAccessToken(LoginRequestDto request) {

        User user = userRepository.findByUsername(request.getUsername());

        if(user == null){
            throw new ResourceNotFoundException("User", "username", request.getUsername());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), user.getAuthorities()));

        // generate access token
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getAuthorities());

        // generate refresh token
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getUsername(), user.getAuthorities());

        // save token with difference versions (WEB, MOBILE, MiniApp) to DB
        tokenRepository.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * Refresh token
     * @param request
     * @return token response
     */
    @Override
    public TokenResponseDto createRefreshToken(HttpServletRequest request) {
        log.info("---------- refreshToken ----------");

        String token = getToken(request);
        String username = jwtService.extractUsername(token, TokenType.REFRESH_TOKEN);
        var user = userRepository.findByUsername(username);
        if(!jwtService.isValid(token, TokenType.REFRESH_TOKEN, user)){
            throw new InvalidDataException("Not allow access with this token");
        }
        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getAuthorities());
        tokenService.saveToken(Token.builder().username(user.getUsername()).accessToken(newAccessToken).refreshToken(token).build());
        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(token)
                .build();
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new InvalidDataException("Token must be not blank");
        }
        if(authorizationHeader.startsWith("Bearer ")){
            // Cắt "Bearer " để lấy token
            return authorizationHeader.substring(7);
        }else{
            throw new InvalidDataException("Token is invalid");
        }
    }

    /**
     * Logout
     * lấy token từ header
     * check token có hợp lệ không
     * xóa token khỏi db
     * @param request
     */
    @Override
    public String logout(HttpServletRequest request) {
        log.info("---------------- Logging out ----------------");
        String token = getToken(request);
        String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        tokenService.deleteToken(username);
        return "Logout success";
    }
}
