package com.lab1.auth.dto;

import com.lab1.users.UserType;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @Size(min = 3, max = 64, message = "Username must be within [3; 64]")
    @NotBlank(message = "Username must not be empty")
    @NotNull
    private String username;

    @Size(min = 3, max = 255, message = "Password length must be within [5; 255]")
    @NotBlank(message = "Password must not be empty")
    @NotNull
    private String password;

    @NotNull(message = "UserType must not be null")
    private UserType userType;
}
