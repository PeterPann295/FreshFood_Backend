package com.freshfood.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    @NotNull(message = "username must be not null")
    private String username;
    @NotNull(message = "password must be not null")
    private String password;
    @NotNull(message = "Device Token must be not null")
    private String deviceToken;
    @NotNull(message = "Version must be not null")
    private String version;
}
