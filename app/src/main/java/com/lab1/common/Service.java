package com.lab1.common;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Service<Resource extends Entity> {
    Resource save(Resource person);
    Page<Resource> getAll(Pageable pageable);
    Optional<Resource> get(Integer id);
    void delete(Integer id);
}
