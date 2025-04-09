package com.eMartix.authservice.dto.request;

import com.eMartix.authservice.common.Gender;
import com.eMartix.authservice.common.UserType;
import com.eMartix.authservice.util.ValidGender;
import com.eMartix.authservice.util.ValidUserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    private LocalDateTime dateOfBirth;

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
