package com.eMartix.auth_service.service.impl;


import com.eMartix.auth_service.model.Token;
import com.eMartix.auth_service.repository.TokenRepository;
import com.eMartix.auth_service.service.TokenService;
import com.eMartix.commons.advice.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    /**
     * Get token by username
     * @param username
     * @return token
     */
    @Override
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException( "token", "username", username));
    }

    /**
     * Save token to database
     * @param token
     */
    @Override
    public void saveToken(Token token) {
        Optional<Token> existsToken = tokenRepository.findByUsername(token.getUsername());
        if(existsToken.isEmpty()){
            tokenRepository.save(token);
        }else{
            Token t = existsToken.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
        }

    }

    /**
     * Delete token by username
     * @param username
     */
    @Override
    public void deleteToken(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }
}
