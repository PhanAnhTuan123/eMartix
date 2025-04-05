package com.eMartix.auth_service.service.impl;

import com.eMartix.auth_service.common.UserStatus;
import com.eMartix.auth_service.dto.response.UserResponseDto;
import com.eMartix.auth_service.model.User;
import com.eMartix.auth_service.repository.RolePermissionRepository;
import com.eMartix.auth_service.repository.UserRepository;
import com.eMartix.auth_service.repository.UserRoleRepository;
import com.eMartix.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getUserDetails(String usernameOrEmail) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("User not found with username or email: " + usernameOrEmail)));

        // Lấy danh sách tên vai trò
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toList());

        // Lấy danh sách quyền từ các vai trò của người dùng
        List<String> permissions = rolePermissionRepository.findByUserId(user.getId()).stream()
                .map(rolePermission -> rolePermission.getPermission().getPermissionName())
                .collect(Collectors.toList());

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setStatus(user.getStatus());
        userResponseDto.setRoles(roles);
        userResponseDto.setPermissions(permissions);
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setDateOfBirth(user.getDateOfBirth());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setType(user.getType());
        return userResponseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> getUserDetails(user.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserResponseDto updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(status);
        User savedUser = userRepository.save(user);
        return getUserDetails(savedUser.getUsername());
    }

}
