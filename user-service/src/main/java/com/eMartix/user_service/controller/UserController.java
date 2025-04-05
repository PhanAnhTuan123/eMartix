package com.eMartix.user_service.controller;

import com.eMartix.user_service.dto.response.UserResponseDto;
import com.eMartix.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping
    public List<UserResponseDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("GET /api/v1/users");
        log.info("------ [getAllUsers] ------");
        return userService.getUsers(pageNo, pageSize, sortBy);

    }


}
