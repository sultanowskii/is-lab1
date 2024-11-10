package com.lab1.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface Service<T extends Entity, TDTo, TCreateDto> {
    TDTo create(TCreateDto obj);
    TDTo update(int id, TCreateDto obj);
    Page<TDTo> getAll(Specification<T> specification, Pageable pageable);
    TDTo get(int id);
    void delete(int id);
}
