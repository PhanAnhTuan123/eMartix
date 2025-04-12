package com.eMartix.authservice.util;

import com.eMartix.authservice.common.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, Gender> {

    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext context) {
        if (gender == null) {
            return false;
        }
        // Kiểm tra nếu giá trị của gender là hợp lệ
        return gender == Gender.MALE || gender == Gender.FEMALE || gender == Gender.OTHER; // Ví dụ với hai giá trị của Gender enum
    }
}