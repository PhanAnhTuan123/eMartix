package com.eMartix.auth_service.service;

import com.eMartix.auth_service.model.Token;

public interface TokenService {
    Token getByUsername(String username);
    void saveToken(Token token);
    void deleteToken(String username);
}
