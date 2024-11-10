package com.lab1.persons;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.lab1.common.CRUDRepository;

@Repository
public interface PersonRepository extends CRUDRepository<Person> {
    Optional<Person> findByName(String name);
}
