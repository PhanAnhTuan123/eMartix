package com.eMartix.authservice.service;

import com.eMartix.authservice.common.UserStatus;
import com.eMartix.authservice.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getUserDetails(String usernameOrEmail);
    List<UserResponseDto> getAllUsers();
    void deleteUser(Long id);
    UserResponseDto updateUserStatus(Long id, UserStatus status);
}
