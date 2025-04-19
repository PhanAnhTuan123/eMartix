package com.eMartix.authservice.service.impl;


import com.eMartix.authservice.service.TokenService;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;
    private static final String OTP_PREFIX = "otp:";
    @Override
    public void storeTokenWithExpiry(String username, String accessToken, String refreshToken) {
        log.info("save accessToken: {} to redis", accessToken);
        redisConnection.sync().setex("accessToken:" + username, TimeUnit.HOURS.toSeconds(1), accessToken);
        redisConnection.sync().setex("refreshToken:" + username, TimeUnit.DAYS.toSeconds(1), refreshToken);
    }

    @Override
    public String getAccessToken(String username) {
        return redisConnection.sync().get("accessToken:" + username);
    }

    @Override
    public String getRefreshToken(String username) {
        return redisConnection.sync().get("refreshToken:" + username);
    }

    @Override
    public void deleteToken(String username) {
        redisConnection.sync().del("accessToken:" + username);
        redisConnection.sync().del("refreshToken:" + username);
    }

    // Lưu token với thời gian hết hạn (ví dụ: 1 giờ)


    @Override
    public void saveOtp(String email, String otp, long ttlMinutes) {
        String key = OTP_PREFIX + email;
        redisConnection.sync().setex(key, ttlMinutes, otp);
    }

    @Override
    public String getOtp(String email) {
        return redisConnection.sync().get(OTP_PREFIX + email);
    }

    @Override
    public void deleteOtp(String email) {
//        redisTemplate.delete(OTP_PREFIX + email);
        redisConnection.sync().del(OTP_PREFIX + email);
    }
}
