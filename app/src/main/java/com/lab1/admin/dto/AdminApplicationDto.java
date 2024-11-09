package com.lab1.admin.dto;

import com.lab1.admin.AdminApplicationStatus;

import lombok.Data;

@Data
public class AdminApplicationDto {
    private int id;
    private int userId;
    private AdminApplicationStatus status;
}
