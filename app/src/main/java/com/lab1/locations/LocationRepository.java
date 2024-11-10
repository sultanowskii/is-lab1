package com.lab1.locations;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.lab1.common.CRUDRepository;

@Repository
public interface LocationRepository extends CRUDRepository<Location> {
    Optional<Location> findByName(String name);
}
