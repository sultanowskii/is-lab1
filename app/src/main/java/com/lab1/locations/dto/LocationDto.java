package com.lab1.locations.dto;

import com.lab1.common.dto.OwnedDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationDto extends OwnedDto {
    private int id;
    private float x;
    private float y;
    private double z;
    private String name;
}
