package com.eMartix.user_service.dto.response;

import com.eMartix.user_service.common.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
}
