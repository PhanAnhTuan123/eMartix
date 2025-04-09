package com.eMartix.authservice.dto.response;

import com.eMartix.authservice.common.Gender;
import com.eMartix.authservice.common.UserStatus;
import com.eMartix.authservice.common.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponseDto {
    private String firstName;

    private String lastName;

    private LocalDateTime dateOfBirth;

    private Gender gender;

    private String phone;

    private String email;

    private String username;

    private UserType type;

    private UserStatus status;

    private List<String> roles;

    @JsonIgnore
    private List<String> permissions;

}
