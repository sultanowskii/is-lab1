package com.lab1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab1.persons.PersonRepository;

@RestController
public class HelloController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("person count: " + personRepository.findAll().size());
        return "{\"message\": \"Hello!\"}";
    }
}
