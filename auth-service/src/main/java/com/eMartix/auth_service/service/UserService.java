package com.eMartix.auth_service.service;

import com.eMartix.auth_service.common.UserStatus;
import com.eMartix.auth_service.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getUserDetails(String usernameOrEmail);
    List<UserResponseDto> getAllUsers();
    void deleteUser(Long id);
    UserResponseDto updateUserStatus(Long id, UserStatus status);
}
