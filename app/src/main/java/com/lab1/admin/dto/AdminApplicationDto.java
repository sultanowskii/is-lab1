package com.lab1.admin.dto;

import com.lab1.admin.AdminApplicationStatus;
import com.lab1.users.User;

import lombok.Data;

@Data
public class AdminApplicationDto {
    private int id;
    private User user;
    private AdminApplicationStatus status;
}
