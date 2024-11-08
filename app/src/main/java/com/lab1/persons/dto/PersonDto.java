package com.lab1.persons.dto;

import com.lab1.persons.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.lab1.common.dto.OwnedDto;

@Data
@EqualsAndHashCode(callSuper=true)
public class PersonDto extends OwnedDto {
    private int id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private int locationId;
    private Integer height;
}
