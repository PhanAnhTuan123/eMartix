package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String userName);
}
