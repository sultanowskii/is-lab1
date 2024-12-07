package com.lab1.imports;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.lab1.common.BaseMapper;
import com.lab1.imports.dto.ImportCreateDto;
import com.lab1.imports.dto.ImportDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ImportMapper implements BaseMapper<Import, ImportDto, ImportCreateDto> {
    public abstract Import toEntity(ImportDto dto);
    public abstract Import toEntityFromCreateDto(ImportCreateDto createDto);
    public abstract ImportDto toDto(ImportDto entity);
    public abstract void update(@MappingTarget Import entity, ImportCreateDto updateDto);
}
