package com.lab1.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ApplicationWebController {
    @GetMapping("/admin/applications")
    public String pageApplications() {
        return "admin/applications";
    }

    @GetMapping("/admin/applications/{id}")
    public String pageApplication() {
        return "admin/application";
    }
}
