package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
