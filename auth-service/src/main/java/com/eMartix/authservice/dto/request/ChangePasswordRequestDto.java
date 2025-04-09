package com.eMartix.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank(message = "Old password is required")
    private String oldPassword;
    @NotBlank(message = "New password is required")
    private String newPassword;
}
