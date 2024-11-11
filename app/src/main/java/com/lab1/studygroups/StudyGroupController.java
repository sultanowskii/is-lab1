package com.lab1.studygroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lab1.common.CRUDController;
import com.lab1.studygroups.dto.*;

@RestController
@RequestMapping("/api/study-groups")
public class StudyGroupController extends CRUDController<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService, StudyGroupSpecification studyGroupSpecification) {
        super(studyGroupService, studyGroupSpecification);
    }
}
