package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService implements com.lab1.common.Service<Person> {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Page<Person> getAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public Optional<Person> get(Integer id) {
        return personRepository.findById(id);
    }

    public Optional<Person> getByName(String name) {
        return personRepository.findByName(name);
    }

    public void delete(Integer id) {
        personRepository.deleteById(id);
    }
}
