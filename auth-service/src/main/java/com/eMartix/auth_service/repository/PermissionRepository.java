package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>{
    Optional<Permission> findByPermissionName(String name);
    boolean existsByPermissionName(String name);
}
