package com.lab1.imports;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ImportWebController {
    @GetMapping("/imports")
    public String pageImports() {
        return "imports/imports";
    }

    @GetMapping("/imports/{id}")
    public String pageImport() {
        return "imports/import";
    }

    @GetMapping("/imports/upload")
    public String pageUpload() {
        return "imports/upload";
    }
}
