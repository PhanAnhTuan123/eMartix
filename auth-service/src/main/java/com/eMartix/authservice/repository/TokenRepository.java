package com.eMartix.authservice.repository;

import com.eMartix.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUsername(String username);
}
