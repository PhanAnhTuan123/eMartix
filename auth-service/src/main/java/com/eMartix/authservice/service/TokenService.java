package com.eMartix.authservice.service;

public interface TokenService {
    void storeTokenWithExpiry(String username, String accessToken, String refreshToken);
    String getAccessToken(String username);
    String getRefreshToken(String username);
    void deleteToken(String username);
     void saveOtp(String email, String otp, long ttlMinutes);
     String getOtp(String email);
     void deleteOtp(String email);
}
