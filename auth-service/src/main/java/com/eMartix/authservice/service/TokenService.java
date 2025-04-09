package com.eMartix.authservice.service;

public interface TokenService {
    void storeToken(String username, String accessToken, String refreshToken);
    String getAccessToken(String username);
    String getRefreshToken(String username);
    void deleteToken(String username);

}
