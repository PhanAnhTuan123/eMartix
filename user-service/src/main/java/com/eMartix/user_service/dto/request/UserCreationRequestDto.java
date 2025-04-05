package com.eMartix.user_service.dto.request;

import com.eMartix.user_service.common.Gender;
import com.eMartix.user_service.common.UserType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserCreationRequestDto implements Serializable {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
    private String password;
    private UserType type;
    //private UserStatus status;
//    private Date createdAt;
//    private Date updatedAt;
}