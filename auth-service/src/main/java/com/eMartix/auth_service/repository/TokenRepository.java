package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUsername(String username);
}
