package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lab1.common.CRUDController;
import com.lab1.persons.dto.*;
import com.lab1.users.UserService;

@RestController
@RequestMapping("/api/persons")
public class PersonController extends CRUDController<Person, PersonDto, PersonCreateDto> {
    @Autowired
    public PersonController(PersonService personService, PersonSpecification personSpecification, UserService userService) {
        super(personService, personSpecification, userService);
    }
}

