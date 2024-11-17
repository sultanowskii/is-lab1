package com.lab1.persons;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PersonWebController {
    @GetMapping("/persons")
    public String pagePersons() {
        return "persons/persons";
    }

    @GetMapping("/persons/create")
    public String pagePersonCreate() {
        return "persons/person-create";
    }

    @GetMapping("/persons/{id}")
    public String pagePerson() {
        return "persons/person";
    }
}
