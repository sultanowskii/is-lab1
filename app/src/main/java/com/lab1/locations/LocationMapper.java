package com.lab1.locations;

import org.mapstruct.*;
import com.lab1.common.BaseMapper;
import com.lab1.locations.dto.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class LocationMapper implements BaseMapper<Location, LocationDto, LocationCreateDto> {
    public abstract Location toEntity(LocationDto dto);
    public abstract Location toEntityFromCreateDto(LocationCreateDto createDto);
    public abstract LocationDto toDto(Location entity);
}
