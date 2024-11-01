package com.freshfood.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNo == null) {
            return false;
        }
        String vietnamesePhoneNumberPattern = "^(03|05|07|08|09)\\d{8}$";
        if (phoneNo.matches(vietnamesePhoneNumberPattern)) {
            return true;
        } else {
            return false;
        }
    }
}
