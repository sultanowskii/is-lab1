package com.lab1.imports;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.lab1.imports.dto.LocationImportDto;
import com.lab1.imports.dto.PersonImportDto;
import com.lab1.imports.dto.StudyGroupImportDto;
import com.lab1.locations.dto.LocationCreateDto;
import com.lab1.persons.dto.PersonCreateDto;
import com.lab1.studygroups.dto.StudyGroupCreateDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ImportMapper {
    public abstract LocationCreateDto toLocationCreateDto(LocationImportDto locationImportDto);
    public abstract LocationImportDto fromLocationCreateDto(LocationCreateDto locationImportDto);

    public abstract PersonCreateDto toPersonCreateDto(PersonImportDto personImportDto);
    public abstract PersonImportDto fromPersonCreateDto(PersonCreateDto personImportDto);

    public abstract StudyGroupCreateDto toStudyGroupCreateDto(StudyGroupImportDto studyGroupImportDto);
    public abstract StudyGroupImportDto fromStudyGroupCreateDto(StudyGroupCreateDto studyGroupImportDto);
}
