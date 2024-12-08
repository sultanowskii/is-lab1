package com.lab1.imports;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.lab1.common.BaseMapper;
import com.lab1.imports.dto.log.ImportLogCreateDto;
import com.lab1.imports.dto.log.ImportLogDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ImportLogMapper implements BaseMapper<ImportLog, ImportLogDto, ImportLogCreateDto> {
    public abstract ImportLog toEntity(ImportLogDto dto);
    public abstract ImportLog toEntityFromCreateDto(ImportLogCreateDto createDto);
    public abstract ImportLogDto toDto(ImportLogDto entity);
    public abstract void update(@MappingTarget ImportLog entity, ImportLogCreateDto updateDto);
}
