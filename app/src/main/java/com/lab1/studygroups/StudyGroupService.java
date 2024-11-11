package com.lab1.studygroups;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.common.CRUDService;
import com.lab1.studygroups.dto.*;
import com.lab1.users.UserService;


@Service
public class StudyGroupService extends CRUDService<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    @Autowired
    public StudyGroupService(UserService userService, StudyGroupRepository studyGroupRepository, StudyGroupMapper studyGroupMapper) {
        super(userService, studyGroupRepository, studyGroupMapper);
    }

    @Override
    @Transactional
    public StudyGroupDto create(StudyGroupCreateDto form) {
        var studyGroup = mapper.toEntityFromCreateDto(form);

        studyGroup.setOwner(userService.getCurrentUser());
        studyGroup.setCreatedAt(ZonedDateTime.now());
        studyGroup.setCreationDate(LocalDateTime.now());

        var createdStudyGroup = repo.save(studyGroup);

        return mapper.toDto(createdStudyGroup);
    }
}
