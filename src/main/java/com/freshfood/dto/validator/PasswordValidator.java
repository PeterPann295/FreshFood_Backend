package com.freshfood.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (password == null || password.length() < 8) {
            return false;  // Phải có ít nhất 8 ký tự
        }

        boolean hasUppercase = false;
        boolean hasSpecialCharacter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialCharacter = true;
            }

            // Nếu thỏa mãn cả ba điều kiện, không cần kiểm tra thêm
            if (hasUppercase && hasSpecialCharacter && hasDigit) {
                return true;
            }
        }

        // Nếu thiếu bất kỳ điều kiện nào, trả về false
        return false;
    }
}
