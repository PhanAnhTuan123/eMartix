package com.eMartix.authservice.service.impl;


import com.eMartix.authservice.service.TokenService;
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
    private static final String OTP_PREFIX = "otp:";
    @Override
    public void storeTokenWithExpiry(String username, String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set("accessToken:" + username, accessToken, 1, TimeUnit.HOURS);  // TTL 1 giờ
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, 7, TimeUnit.DAYS);  // TTL 7 ngày
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


    @Override
    public void saveOtp(String email, String otp, long ttlMinutes) {
        String key = OTP_PREFIX + email;
        redisTemplate.opsForValue().set(key, otp, ttlMinutes, TimeUnit.MINUTES);
    }

    @Override
    public String getOtp(String email) {
        return (String) redisTemplate.opsForValue().get(OTP_PREFIX + email);
    }

    @Override
    public void deleteOtp(String email) {
        redisTemplate.delete(OTP_PREFIX + email);
    }
}
