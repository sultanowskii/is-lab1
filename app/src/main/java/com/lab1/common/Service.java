package com.lab1.common;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.lab1.common.paging.Paginator;

public interface Service<T extends Entity, TDTo, TCreateDto> {
    TDTo create(TCreateDto obj);
    TDTo update(int id, TCreateDto obj);
    Page<TDTo> getAll(Specification<T> specification, Paginator paginator);
    TDTo get(int id);
    void delete(int id);
}
