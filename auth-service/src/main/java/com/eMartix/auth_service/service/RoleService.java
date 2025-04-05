package com.eMartix.auth_service.service;

import com.eMartix.auth_service.model.Permission;
import com.eMartix.auth_service.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String name);
    Role createRole(Role role);
    void deleteRole(Long id);
    List<Permission> getRolePermissions(Long roleId);
    void addPermissionToRole(Long roleId, Long permissionId);void removePermissionFromRole(Long roleId, Long permissionId);
    Permission createPermission(Permission permission);
    List<Permission> getAllPermissions();
}
