package com.lab1.imports.dto.log;

import com.lab1.imports.Status;
import com.lab1.users.User;

import lombok.Data;

@Data
public class ImportLogDto {
    private int id;
    private User performer;
    private String fileKey;
    private int createdCount;
    private Status status;
}
