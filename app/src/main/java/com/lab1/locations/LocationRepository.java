package com.lab1.locations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import com.lab1.common.CRUDRepository;

@Repository
public interface LocationRepository extends CRUDRepository<Location> {
    Optional<Location> findByName(String name);

    @Override
    @EntityGraph(attributePaths = { "owner", "updatedBy" })
    List<Location> findAll(Specification<Location> spec);
}
