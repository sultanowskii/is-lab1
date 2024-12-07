package com.lab1.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.ValidationException;
import com.lab1.common.paging.PaginationMapper;
import com.lab1.common.paging.dto.PaginationDto;
import com.lab1.users.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

public class CRUDController<T extends OwnedEntity, TDto, TCreateDto> {
    private final CRUDService<T, TDto, TCreateDto> service;
    private CRUDSpecification<T> specBuilder;
    private final UserService userService;

    @Autowired
    private PaginationMapper paginationMapper;

    public CRUDController(CRUDService<T, TDto, TCreateDto> service, CRUDSpecification<T> specBuilder, UserService userService) {
        this.service = service;
        this.specBuilder = specBuilder;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create an object", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> create(@Valid @RequestBody TCreateDto form) {
        return ResponseEntity.status(201).body(service.create(form));
    }

    @GetMapping
    @Operation(summary = "Get all objects", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Page<TDto>> getAll(SearchParamsDto searchParamsDto, @PageableDefault(size = 20) PaginationDto pagiationDto) throws ValidationException {
        searchParamsDto.validate();
        var user = userService.getCurrentUser();
        Specification<T> spec = specBuilder.build(user);
        if (!searchParamsDto.isEmpty()) {
            spec = specBuilder.buildWithSearchParams(user, searchParamsDto);
        }
        final var paginator = paginationMapper.toEntity(pagiationDto);
        return ResponseEntity.ok().body(service.getAll(spec, paginator));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<TDto> update(@PathVariable("id") int id, @Valid @RequestBody TCreateDto form) {
        return ResponseEntity.ok(service.update(id, form));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete object by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
