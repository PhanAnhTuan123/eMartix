package com.eMartix.authservice.repository;

import com.eMartix.authservice.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<RolePermission> findByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Query("SELECT rp FROM RolePermission rp JOIN rp.role r JOIN UserRole ur ON ur.role.id = r.id WHERE ur.user.id = :userId")
    List<RolePermission> findByUserId(@Param("userId") Long userId);
}
