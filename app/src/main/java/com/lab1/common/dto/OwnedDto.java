package com.lab1.common.dto;

import java.time.ZonedDateTime;
import com.lab1.users.User;

import lombok.Data;

@Data
public abstract class OwnedDto {
    private User owner;
    private ZonedDateTime createdAt;
    private User updatedBy;
    private ZonedDateTime updatedAt;
}
