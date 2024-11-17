package com.lab1.studygroups;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StudyGroupWebController {
    @GetMapping("/study-groups")
    public String pageStudyGroups() {
        return "study-groups/study-groups";
    }

    @GetMapping("/study-groups/create")
    public String pageStudyGroupCreate() {
        return "study-groups/study-group-create";
    }

    @GetMapping("/study-groups/{id}")
    public String pageStudyGroup() {
        return "study-groups/study-group";
    }
}
