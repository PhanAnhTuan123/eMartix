package com.eMartix.auth_service.seeder;

import com.eMartix.auth_service.common.UserStatus;
import com.eMartix.auth_service.model.*;
import com.eMartix.auth_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Tạo quyền (Permissions)
        createPermissionsIfNotFound();

        // Tạo vai trò (Roles)
        createRolesIfNotFound();

        // Liên kết Roles và Permissions
        assignPermissionsToRoles();

        // Tạo tài khoản admin
        createAdminUserIfNotFound();
    }

    private void createPermissionsIfNotFound() {
        List<String> permissionNames = Arrays.asList(
                "user:read", "user:write", "user:delete",
                "role:read", "role:write", "role:delete",
                "permission:read", "permission:write", "permission:delete"
        );

        for (String name : permissionNames) {
            if (!permissionRepository.existsByPermissionName(name)) {
                Permission permission = new Permission();
                permission.setPermissionName(name);
                permission.setPermissionDescription("Permission to " + name.replace(":", " "));
                permissionRepository.save(permission);
            }
        }
    }

    private void createRolesIfNotFound() {
        // Vai trò ADMIN
        if (!roleRepository.existsByRoleName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            adminRole.setRoleDescription("Administrator role with all permissions");
            roleRepository.save(adminRole);
        }

        // Vai trò USER
        if (!roleRepository.existsByRoleName("USER")) {
            Role userRole = new Role();
            userRole.setRoleName("USER");
            userRole.setRoleDescription("Regular user role with limited permissions");
            roleRepository.save(userRole);
        }
    }

    private void assignPermissionsToRoles() {
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow();
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow();

        // Gán tất cả quyền cho ADMIN
        List<Permission> allPermissions = permissionRepository.findAll();
        for (Permission permission : allPermissions) {
            if (rolePermissionRepository.findByRoleId(adminRole.getId()).stream()
                    .noneMatch(rp -> rp.getPermission().getId().equals(permission.getId()))) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(adminRole);
                rolePermission.setPermission(permission);
                rolePermissionRepository.save(rolePermission);
            }
        }

        // Gán một số quyền cơ bản cho USER
        List<String> userPermissions = Arrays.asList("user:read", "role:read", "permission:read");
        for (String permName : userPermissions) {
            Permission permission = permissionRepository.findByPermissionName(permName).orElseThrow();
            if (rolePermissionRepository.findByRoleId(userRole.getId()).stream()
                    .noneMatch(rp -> rp.getPermission().getId().equals(permission.getId()))) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(userRole);
                rolePermission.setPermission(permission);
                rolePermissionRepository.save(rolePermission);
            }
        }
    }

    private void createAdminUserIfNotFound() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setStatus(UserStatus.ACTIVE);
            User savedAdmin = userRepository.save(admin);

            Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow();
            UserRole userRole = new UserRole();
            userRole.setUser(savedAdmin);
            userRole.setRole(adminRole);
            userRoleRepository.save(userRole);
        }
    }
}