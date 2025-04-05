package com.eMartix.auth_service.dto.request;

import com.eMartix.auth_service.common.Gender;
import com.eMartix.auth_service.common.UserStatus;
import com.eMartix.auth_service.common.UserType;
import com.eMartix.auth_service.model.Role;
import com.eMartix.auth_service.util.ValidGender;
import com.eMartix.auth_service.util.ValidUserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    private LocalDateTime dateOfBirth;

//    @Pattern(regexp = "male|female|other", message = "type should be MALE, FEMALE, OTHER")
    @ValidGender
    private Gender gender;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phone;

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

//    @Pattern(regexp = "owner|admin|user", message = "Type should be either OWNER, ADMIN or USER")
    @ValidUserType
    private UserType type;

    private List<String> roles;
}
