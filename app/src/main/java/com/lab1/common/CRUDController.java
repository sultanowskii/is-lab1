package com.lab1.common;

import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CRUDController<T extends OwnedEntity, TDto, TCreateDto> {
    private final CRUDService<T> service;
    private final BaseMapper<T, TDto, TCreateDto> mapper;

    @PostMapping
    @Operation(summary = "Create an object", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> create(@Valid @RequestBody TCreateDto form) {
        var obj = mapper.toEntityFromCreateDto(form); 
        var createdObj = service.create(obj);
        return ResponseEntity.status(201).body(mapper.toDto(createdObj));
    }

    @GetMapping
    @Operation(summary = "Get all objects", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Page<TDto>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        var objs = service.getAll(pageable).map(o -> mapper.toDto(o));
        return ResponseEntity.ok().body(objs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> getById(@PathVariable("id") int id) {
        Optional<T> obj = service.get(id);
        if (!obj.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDto(obj.get()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> update(@PathVariable("id") int id, @Valid @RequestBody TCreateDto form) {
        Optional<T> existingObj = service.get(id);
        if (!existingObj.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        var objToUpdate = mapper.toEntityFromCreateDto(form);
        objToUpdate.setId(id);
        var updatedObj = service.update(id, objToUpdate);
        return ResponseEntity.ok(mapper.toDto(updatedObj));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
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
