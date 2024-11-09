package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab1.common.CRUDService;
import com.lab1.users.UserService;


@Service
public class PersonService extends CRUDService<Person> {
    @Autowired
    public PersonService(UserService userService, PersonRepository personRepository) {
        super(userService, personRepository);
    }
}
