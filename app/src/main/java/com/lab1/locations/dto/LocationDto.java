package com.lab1.locations.dto;

import lombok.Data;

@Data
public class LocationDto {
    private int id;
    private float x;
    private float y;
    private double z;
    private String name;
}
