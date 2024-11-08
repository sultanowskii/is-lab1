package com.lab1.common;

import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CRUDController<T extends Entity, TDto, TCreateDto> {
    private final Service<T> service;
    private final BaseMapper<T, TDto, TCreateDto> mapper;

    @PostMapping
    public ResponseEntity<TDto> create(@Valid @RequestBody TCreateDto form) {
        var obj = mapper.toEntityFromCreateDto(form); 
        var createdObj = service.save(obj);
        return ResponseEntity.status(201).body(mapper.toDto(createdObj));
    }

    @GetMapping
    public ResponseEntity<Page<TDto>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        var objs = service.getAll(pageable).map(o -> mapper.toDto(o));
        return ResponseEntity.ok().body(objs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TDto> getById(@PathVariable("id") int id) {
        Optional<T> obj = service.get(id);
        if (!obj.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDto(obj.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TDto> update(@PathVariable("id") int id, @Valid @RequestBody TCreateDto form) {
        Optional<T> existingObj = service.get(id);
        if (!existingObj.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        var objToUpdate = mapper.toEntityFromCreateDto(form);
        objToUpdate.setId(id);
        var updatedObj = service.save(objToUpdate);
        return ResponseEntity.ok(mapper.toDto(updatedObj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        Optional<T> existingObj = service.get(id);
        if (existingObj.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
