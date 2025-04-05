package com.eMartix.user_service.service;

import com.eMartix.user_service.dto.request.UserCreationRequestDto;
import com.eMartix.user_service.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    String addUser(UserCreationRequestDto dto);

//    void updateUser(UserUpdateDTO dto);
//
//    void changePassword(PwdChangeRequestDTO dto);

    void deleteUser(long userId);

    UserResponseDto getUserDetails(long userId);

    List<UserResponseDto> getUsers(int pageNo, int pageSize, String sort);

}
