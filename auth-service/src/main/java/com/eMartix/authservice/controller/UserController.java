package com.eMartix.authservice.controller;


import com.eMartix.authservice.common.UserStatus;
import com.eMartix.authservice.dto.response.UserResponseDto;
import com.eMartix.authservice.model.Permission;
import com.eMartix.authservice.model.Role;
import com.eMartix.authservice.service.RoleService;
import com.eMartix.authservice.service.UserService;
import com.eMartix.commons.dtos.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(userService.getUserDetails(currentUsername));
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> status) {
        UserResponseDto user = userService.updateUserStatus(id, UserStatus.valueOf(status.get("status") ? "ACTIVE" : "INACTIVE"));
        return ResponseEntity.ok(new ApiResponse(true, "User status updated successfully", user));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(new ApiResponse(true, "Role created successfully", createdRole));
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(roleService.getAllPermissions());
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = roleService.createPermission(permission);
        return ResponseEntity.ok(new ApiResponse(true, "Permission created successfully", createdPermission));
    }

    @GetMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getRolePermissions(roleId));
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addPermissionToRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse(true, "Permission added to role successfully"));
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        roleService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse(true, "Permission removed from role successfully"));
    }
}