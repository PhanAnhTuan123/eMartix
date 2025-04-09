package com.eMartix.authservice.dto.request;

import com.eMartix.authservice.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProfileRequestDto {
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDateTime dateOfBirth;
    private Gender gender;
}
