package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lab1.persons.dto.*;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;
    private final PersonMapper personMapper;

    @Autowired
    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@Valid @RequestBody PersonCreateDto personForm) {
        var person = personMapper.toEntityFromCreateDto(personForm);
        var savedPerson = personService.savePerson(person);
        return ResponseEntity.status(201).body(personMapper.toDto(savedPerson));
    }

    @GetMapping
    public ResponseEntity<Page<PersonDto>> getAllPersons(@PageableDefault(size = 20) Pageable pageable) {
        var persons = personService.getAllPersons(pageable).map(p -> personMapper.toDto(p));
        return ResponseEntity.ok().body(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable("id") int id) {
        Optional<Person> person = personService.getPersonById(id);
        if (!person.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personMapper.toDto(person.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable("id") int id, @Valid @RequestBody PersonCreateDto personForm) {
        Optional<Person> existingPerson = personService.getPersonById(id);
        if (!existingPerson.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        var personToUpdate = personMapper.toEntityFromCreateDto(personForm);
        personToUpdate.setId(id);
        Person updatedPerson = personService.savePerson(personToUpdate);
        return ResponseEntity.ok(personMapper.toDto(updatedPerson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) {
        Optional<Person> existingPerson = personService.getPersonById(id);
        if (existingPerson.isPresent()) {
            personService.deletePerson(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Person> getPersonByName(@PathVariable("name") String name) {
        Optional<Person> person = personService.getPersonByName(name);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

