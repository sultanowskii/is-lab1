package com.lab1.common;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Service<T extends Entity> {
    T create(T obj);
    T update(int id, T obj);
    Page<T> getAll(Pageable pageable);
    Optional<T> get(int id);
    void delete(int id);
}
