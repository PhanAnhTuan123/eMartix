package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
    boolean existsByRoleName(String name);
}
