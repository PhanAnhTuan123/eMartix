package com.eMartix.user_service.service.impl;

import com.eMartix.commons.advice.ResourceNotFoundException;
import com.eMartix.user_service.common.UserStatus;
import com.eMartix.user_service.dto.request.UserCreationRequestDto;
import com.eMartix.user_service.dto.response.UserResponseDto;
import com.eMartix.user_service.model.User;
import com.eMartix.user_service.repository.UserRepository;
import com.eMartix.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String addUser(UserCreationRequestDto dto) {
        return "";
    }

    @Override
    public void deleteUser(long userId) {

    }

    @Override
    public UserResponseDto getUserDetails(long userId) {
        return null;
    }

    @Override
    public List<UserResponseDto> getUsers(int page, int size, String sort) {
        log.info(" Service -----[ getUsers ]-----");

        int number = 0;
        if (page > 0) {
            number = page - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (StringUtils.hasLength(sort)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(number, size, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        return users.stream().filter(
                        user -> user.getStatus() == UserStatus.ACTIVE
                )
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .username(user.getUsername())
                        .build())
                .toList();
    }

    /**
     * Get user by ID
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
        log.info("-----[ getUser ]-----");
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
