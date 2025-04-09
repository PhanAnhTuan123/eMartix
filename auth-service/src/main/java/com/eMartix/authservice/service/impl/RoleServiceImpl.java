package com.eMartix.authservice.service.impl;


import com.eMartix.authservice.model.Permission;
import com.eMartix.authservice.model.Role;
import com.eMartix.authservice.model.RolePermission;
import com.eMartix.authservice.repository.PermissionRepository;
import com.eMartix.authservice.repository.RolePermissionRepository;
import com.eMartix.authservice.repository.RoleRepository;
import com.eMartix.authservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    @Transactional
    @Override
    public Role createRole(Role role) {
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new RuntimeException("Role already exists with name: " + role.getRoleName());
        }
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId).stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermissionRepository.save(rolePermission);
    }

    @Transactional
    @Override
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }

    @Transactional
    @Override
    public Permission createPermission(Permission permission) {
        if (permissionRepository.existsByPermissionName(permission.getPermissionName())) {
            throw new RuntimeException("Permission already exists with name: " + permission.getPermissionName());
        }
        return permissionRepository.save(permission);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
