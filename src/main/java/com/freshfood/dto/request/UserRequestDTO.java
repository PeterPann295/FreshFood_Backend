package com.freshfood.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freshfood.dto.validator.Email;
import com.freshfood.dto.validator.EnumPattern;
import com.freshfood.dto.validator.Password;
import com.freshfood.dto.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.freshfood.util.UserStatus;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserRequestDTO implements Serializable {
    @NotBlank(message = "username must be not blank")
    @Length(min = 8, message = "username must be at least 8 character long")
    private String username;

    @NotBlank(message = "password must be not blank")
    @Password
    private String password;

    @Email
    private String email;

    @PhoneNumber
    private String phone;

    @NotNull(message = "Date of birth must be not null")
    @JsonFormat(pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    private UserStatus status;
}
