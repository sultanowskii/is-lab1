package com.lab1.studygroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab1.common.CRUDService;
import com.lab1.studygroups.dto.*;
import com.lab1.users.UserService;


@Service
public class StudyGroupService extends CRUDService<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    @Autowired
    public StudyGroupService(UserService userService, StudyGroupRepository studyGroupRepository, StudyGroupMapper studyGroupMapper) {
        super(userService, studyGroupRepository, studyGroupMapper);
    }
}
