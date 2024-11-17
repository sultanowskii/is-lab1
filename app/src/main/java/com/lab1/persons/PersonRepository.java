package com.lab1.persons;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import com.lab1.common.CRUDRepository;

@Repository
public interface PersonRepository extends CRUDRepository<Person> {
    Optional<Person> findByName(String name);

    @Override
    @EntityGraph(attributePaths = { "owner", "updatedBy" })
    List<Person> findAll(Specification<Person> spec);
}
