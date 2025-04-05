package com.eMartix.auth_service.util;

import com.eMartix.auth_service.common.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserTypeValidator implements ConstraintValidator<ValidUserType, UserType> {
    @Override
    public boolean isValid(UserType userType, ConstraintValidatorContext context) {
        if (userType == null) {
            return false;
        }
        // Kiểm tra nếu giá trị của userType là hợp lệ
        return userType == UserType.USER || userType == UserType.ADMIN || userType == UserType.OWNER; // Ví dụ với hai giá trị của UserType enum
    }
}
