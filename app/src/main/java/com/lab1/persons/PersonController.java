package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lab1.common.CRUDController;
import com.lab1.persons.dto.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController extends CRUDController<Person, PersonDto, PersonCreateDto> {
    @Autowired
    public PersonController(PersonService personService, PersonSpecification personSpecification) {
        super(personService, personSpecification);
    }
}

