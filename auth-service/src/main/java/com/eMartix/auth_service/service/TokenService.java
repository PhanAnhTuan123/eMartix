package com.eMartix.auth_service.service;

import com.eMartix.auth_service.model.Token;

public interface TokenService {
    void storeToken(String username, String accessToken, String refreshToken);
    String getAccessToken(String username);
    String getRefreshToken(String username);
    void deleteToken(String username);

}
