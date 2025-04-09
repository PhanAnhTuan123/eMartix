package com.eMartix.authservice.service.impl;

import com.eMartix.authservice.common.UserStatus;
import com.eMartix.authservice.dto.request.ChangePasswordRequestDto;
import com.eMartix.authservice.dto.request.UpdateProfileRequestDto;
import com.eMartix.authservice.dto.response.UserResponseDto;
import com.eMartix.authservice.model.User;
import com.eMartix.authservice.repository.RolePermissionRepository;
import com.eMartix.authservice.repository.UserRepository;
import com.eMartix.authservice.repository.UserRoleRepository;
import com.eMartix.authservice.service.UserService;
import com.eMartix.commons.advice.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    @Override
    public void changePassword(String username, ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (changePasswordRequestDto.getOldPassword().equals(changePasswordRequestDto.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateProfile(Long userId, UpdateProfileRequestDto updateProfileRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if(updateProfileRequestDto.getFirstName() != null) {
            user.setFirstName(updateProfileRequestDto.getFirstName());
        }

        if(updateProfileRequestDto.getLastName() != null) {
            user.setLastName(updateProfileRequestDto.getLastName());
        }

        if(updateProfileRequestDto.getDateOfBirth() != null) {
            user.setDateOfBirth(updateProfileRequestDto.getDateOfBirth());
        }

        if(updateProfileRequestDto.getGender() != null) {
            user.setGender(updateProfileRequestDto.getGender());
        }

        if(updateProfileRequestDto.getPhone() != null) {
            user.setPhone(updateProfileRequestDto.getPhone());
        }

        userRepository.save(user);
    }
}
