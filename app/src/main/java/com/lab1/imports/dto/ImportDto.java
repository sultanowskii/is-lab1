package com.lab1.imports.dto;

import com.lab1.imports.Status;
import com.lab1.users.User;

import lombok.Data;

@Data
public class ImportDto {
    private int id;
    private User performer;
    private int createdCount;
    private Status status;
}
