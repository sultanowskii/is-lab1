package com.lab1.common;

public interface BaseMapper<T, TDto, TCreateDto>  {
    T toEntity(TDto dto);
    T toEntityFromCreateDto(TCreateDto createDto);
    TDto toDto(T entity);
}
