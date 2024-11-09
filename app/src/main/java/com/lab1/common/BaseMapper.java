package com.lab1.common;

import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends Entity, TDto, TCreateDto>  {
    T toEntity(TDto dto);
    T toEntityFromCreateDto(TCreateDto createDto);
    TDto toDto(T entity);
    void update(@MappingTarget T entity, TCreateDto updateDto);
}
