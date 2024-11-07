package com.lab1.persons.dto;

import com.lab1.persons.Color;
import lombok.Data;
// import lombok.EqualsAndHashCode;
// import com.lab1.common.dto.OwnedEntityDto;

@Data
// @EqualsAndHashCode(callSuper=true)
// public class PersonDto extends OwnedEntityDto {
public class PersonDto {
    private int id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private int locationId;
    private Integer height;
}
