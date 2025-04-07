package com.eMartix.auth_service.service.impl;

import com.eMartix.auth_service.common.UserStatus;
import com.eMartix.auth_service.dto.request.LoginRequestDto;
import com.eMartix.auth_service.dto.request.RegisterRequestDto;
import com.eMartix.auth_service.dto.response.LoginResponse;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import com.eMartix.auth_service.exception.InvalidDataException;
import com.eMartix.auth_service.helper.JwtTokenProvider;
import com.eMartix.auth_service.model.Role;
import com.eMartix.auth_service.model.User;
import com.eMartix.auth_service.model.UserRole;
import com.eMartix.auth_service.repository.RoleRepository;
import com.eMartix.auth_service.repository.UserRepository;
import com.eMartix.auth_service.repository.UserRoleRepository;
import com.eMartix.auth_service.service.AuthenticationService;
import com.eMartix.auth_service.service.TokenService;
import com.eMartix.auth_service.service.UserDetailsServiceImpl;
import com.eMartix.auth_service.service.UserService;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;


    @Override
    public LoginResponse authenticateUser(LoginRequestDto request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  // Chỉ có thể truy cập từ server, bảo vệ khỏi XSS
        refreshTokenCookie.setSecure(true);    // Chỉ gửi qua HTTPS
        refreshTokenCookie.setPath("/");       // Gửi trong các yêu cầu tới toàn bộ ứng dụng
        response.addCookie(refreshTokenCookie);
        tokenService.storeToken(authentication.getName(), jwt, refreshToken);
        return new LoginResponse(jwt);
    }


    @Override
    public LoginResponse createRefreshToken(String token) {
        // phan giai claims -> lay sub
        String username = tokenProvider.getUsernameFromToken(token);
        // Kiểm tra Refresh Token trong Redis
        String storedToken = tokenService.getRefreshToken(username);
        if (storedToken != null && storedToken.equals(token)) {
            UserDetails exitsUser = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(exitsUser, null, exitsUser.getAuthorities());
            // Nếu tồn tại, tạo mới Refresh Token và Access Token
            String newAccessToken = tokenProvider.generateToken(authentication);
            String newRefreshToken = tokenProvider.generateRefreshToken(authentication);
            // Lưu Refresh Token mới vào Redis
            tokenService.storeToken(username, newAccessToken, newRefreshToken);
            return new LoginResponse(newAccessToken);
        }
        throw new JwtException("Refresh token is invalid");
    }

    /**
     * Refresh token
     * @param request
     * @return token response
     */



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
     * @param
     */
//    @Override
//    public String logout(HttpServletRequest request) {
//        log.info("---------------- Logging out ----------------");
//        String token = getToken(request);
//        String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
//        tokenService.deleteToken(username);
//        return "Logout success";
//    }

    @Transactional
    public UserResponseDto register(RegisterRequestDto registerRequest) {
        // Check if username is taken
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email is taken
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setPhone(registerRequest.getPhone());
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setGender(registerRequest.getGender());
        user.setType(registerRequest.getType());

        User savedUser = userRepository.save(user);

        // Assign roles to user
        List<String> roleNames = registerRequest.getRoles();
        if (roleNames == null || roleNames.isEmpty()) {
            roleNames = Collections.singletonList("USER"); // Default role
        }

        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            UserRole userRole = new UserRole();
            userRole.setUser(savedUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return userService.getUserDetails(savedUser.getUsername());
    }

}
