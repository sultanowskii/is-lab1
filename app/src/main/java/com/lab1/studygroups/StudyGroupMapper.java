package com.lab1.studygroups;

import org.mapstruct.*;
import com.lab1.common.BaseMapper;
import com.lab1.studygroups.dto.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class StudyGroupMapper implements BaseMapper<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    public abstract StudyGroup toEntity(StudyGroupDto dto);
    public abstract StudyGroup toEntityFromCreateDto(StudyGroupCreateDto createDto);
    public abstract StudyGroupDto toDto(StudyGroup entity);
}
