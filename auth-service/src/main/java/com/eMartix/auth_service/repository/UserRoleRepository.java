package com.eMartix.auth_service.repository;

import com.eMartix.auth_service.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(Long userId);

    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}
