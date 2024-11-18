package com.lab1.extra.studygroups;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/extra/study-groups")
public class StudyGroupExtraWebController {
    @GetMapping("")
    public String extraIndex() {
        return "extra/index";
    }

    @GetMapping("/delete-with-average-mark")
    public String deleteWithAverageMark() {
        return "extra/delete-with-average-mark";
    }

    @GetMapping("/count-with-average-mark")
    public String countWithAverageMark() {
        return "extra/count-with-average-mark";
    }

    @GetMapping("/find-with-name-like")
    public String findWithNameLike() {
        return "extra/find-with-name-like";
    }

    @GetMapping("/count-total-expelled-students")
    public String countTotalExpelledStudents() {
        return "extra/count-total-expelled-students";
    }

    @GetMapping("/change-form-of-education")
    public String changeFormOfEducation() {
        return "extra/change-form-of-education";
    }
}
