package com.lab1.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Service<T extends Entity, TDTo, TCreateDto> {
    TDTo create(TCreateDto obj);
    TDTo update(int id, TCreateDto obj);
    Page<TDTo> getAll(Pageable pageable);
    TDTo get(int id);
    void delete(int id);
}
