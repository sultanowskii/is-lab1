package com.lab1.auth.dto;

import com.lab1.users.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDto {
    private User user;
    private String token;
}
