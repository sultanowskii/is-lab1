package com.lab1.persons;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.lab1.common.BaseMapper;
import com.lab1.locations.*;
import com.lab1.persons.dto.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class PersonMapper implements BaseMapper<Person, PersonDto, PersonCreateDto> {
    @Autowired
    protected LocationRepository locationRepository;

    @Mapping(source = "dto.locationId", qualifiedByName = "mapLocation", target = "location")
    public abstract Person toEntity(PersonDto dto);
    @Mapping(source = "createDto.locationId", qualifiedByName = "mapLocation", target = "location")
    public abstract Person toEntityFromCreateDto(PersonCreateDto createDto);
    @Mapping(source = "entity.location", qualifiedByName = "mapLocationId", target = "locationId")
    public abstract PersonDto toDto(Person entity);

    @Named("mapLocation") 
    protected Location mapLocation(int locationId) {
        return locationRepository.findById(locationId).orElse(null);
    }

    @Named("mapLocationId") 
    protected Integer mapLocationId(Location location) {
        if (location == null) {
            return null;
        }
        return location.getId();
    }
}
