package com.lab1.common.paging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaginationDto {
    @NotNull
    int page;

    @NotNull
    int size;

    @NotNull
    List<String> sort;
}
