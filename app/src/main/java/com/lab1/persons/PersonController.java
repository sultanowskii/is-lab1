package com.lab1.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Создание нового Person
    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        Person savedPerson = personService.savePerson(person);
        return ResponseEntity.status(201).body(savedPerson);  // HTTP статус 201 (Created)
    }

    // Получение всех Person
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    // Получение Person по ID
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Optional<Person> person = personService.getPersonById(id);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Обновление Person по ID
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") int id, @Valid @RequestBody Person personDetails) {
        Optional<Person> existingPerson = personService.getPersonById(id);
        if (existingPerson.isPresent()) {
            personDetails.setId(id);  // Устанавливаем ID, чтобы обновить существующую запись
            Person updatedPerson = personService.savePerson(personDetails);
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.notFound().build();  // Если Person не найден, возвращаем 404
        }
    }

    // Удаление Person по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) {
        Optional<Person> existingPerson = personService.getPersonById(id);
        if (existingPerson.isPresent()) {
            personService.deletePerson(id);
            return ResponseEntity.noContent().build();  // HTTP статус 204 (No Content)
        } else {
            return ResponseEntity.notFound().build();  // Если Person не найден, возвращаем 404
        }
    }

    // Поиск Person по имени
    @GetMapping("/name/{name}")
    public ResponseEntity<Person> getPersonByName(@PathVariable("name") String name) {
        Optional<Person> person = personService.getPersonByName(name);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

