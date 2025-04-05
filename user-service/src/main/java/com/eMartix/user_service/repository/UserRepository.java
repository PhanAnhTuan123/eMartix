package com.eMartix.user_service.repository;

import com.eMartix.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String userName);
}
