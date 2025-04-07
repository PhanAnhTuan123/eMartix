package com.eMartix.auth_service.service.impl;


import com.eMartix.auth_service.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void storeToken(String username, String accessToken, String refreshToken) {
        // Lưu access token với key "accessToken:{username}"
        redisTemplate.opsForValue().set("accessToken:" + username, accessToken);
        // Lưu refresh token với key "refreshToken:{username}"
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken);
    }

    @Override
    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get("accessToken:" + username);
    }

    @Override
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refreshToken:" + username);
    }

    @Override
    public void deleteToken(String username) {
        redisTemplate.delete("accessToken:" + username);
        redisTemplate.delete("refreshToken:" + username);
    }

    // Lưu token với thời gian hết hạn (ví dụ: 1 giờ)
    public void storeTokenWithExpiry(String username, String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set("accessToken:" + username, accessToken, 1, TimeUnit.HOURS);  // TTL 1 giờ
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, 7, TimeUnit.DAYS);  // TTL 7 ngày
    }
}
