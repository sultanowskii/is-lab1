package com.lab1.studygroups;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.lab1.common.BaseMapper;
import com.lab1.common.error.ValidationException;
import com.lab1.persons.Person;
import com.lab1.persons.PersonRepository;
import com.lab1.studygroups.dto.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class StudyGroupMapper implements BaseMapper<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    @Autowired
    protected PersonRepository personRepository;

    @Mapping(source = "dto.groupAdminId", qualifiedByName = "mapGroupAdmin", target = "groupAdmin")
    public abstract StudyGroup toEntity(StudyGroupDto dto);

    @Mapping(source = "createDto.groupAdminId", qualifiedByName = "mapGroupAdmin", target = "groupAdmin")
    public abstract StudyGroup toEntityFromCreateDto(StudyGroupCreateDto createDto);

    @Mapping(source = "entity.groupAdmin", qualifiedByName = "mapGroupAdminId", target = "groupAdminId")
    public abstract StudyGroupDto toDto(StudyGroup entity);

    @Named("mapGroupAdmin")
    protected Person mapGroupAdmin(int groupAdminId) {
        return personRepository
            .findById(groupAdminId)
            .orElseThrow(() -> new ValidationException("Person with id=" + groupAdminId + " (groupAdminId) not found"));
    }

    @Named("mapGroupAdminId")
    protected int mapGroundAdminId(Person groupAdmin) {
        if (groupAdmin == null) {
            return 0;
        }
        return groupAdmin.getId();
    }
}
