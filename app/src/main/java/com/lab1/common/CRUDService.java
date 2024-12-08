package com.lab1.common;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.common.error.ValidationException;
import com.lab1.common.paging.Paginator;
import com.lab1.common.paging.SmartPage;
import com.lab1.common.error.NotFoundException;
import com.lab1.common.error.PermissionDeniedException;
import com.lab1.users.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CRUDService<T extends OwnedEntity, TDto, TCreateDto> implements com.lab1.common.Service<T, TDto, TCreateDto> {
    protected final UserService userService;
    protected final CRUDRepository<T> repo;
    protected final BaseMapper<T, TDto, TCreateDto> mapper;


    protected void setPreCreateDetails(T obj) {
        obj.setOwner(userService.getCurrentUser());
        obj.setCreatedAt(ZonedDateTime.now());
    }

    @Transactional
    public TDto create(TCreateDto form) {
        var obj = mapper.toEntityFromCreateDto(form);

        setPreCreateDetails(obj);
        
        var createdObj = repo.save(obj);

        return mapper.toDto(createdObj);
    }

    @Transactional
    public List<TDto> createAll(List<TCreateDto> forms) {
        var objs = forms
            .stream()
            .map(form -> mapper.toEntityFromCreateDto(form))
            .toList();
        objs.forEach(this::setPreCreateDetails);

        var createdObjs = repo.saveAll(objs);

        return createdObjs
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Transactional
    public TDto update(int id, TCreateDto form) {
        var obj = repo
            .findById(id)
            .orElseThrow(() -> new ValidationException("Resource with id=" + id + " not found"));

        var currentUser = userService.getCurrentUser();

        if (obj.getOwner() == currentUser || currentUser.isAdmin()) {
            mapper.update(obj, form);
            obj.setUpdatedBy(userService.getCurrentUser());
            obj.setUpdatedAt(ZonedDateTime.now());
            var updatedObj = repo.save(obj);
            return mapper.toDto(updatedObj);
        }

        throw new PermissionDeniedException("You can't update this resource");
    }

    public Page<TDto> getAll(Specification<T> specification, Paginator paginator) {
        final var allObject = repo.findAll(specification, paginator.getSort());
        final var paged = new SmartPage<>(allObject, paginator);
        return paged.map(o -> mapper.toDto(o));
    }

    public TDto get(int id) {
        var obj = repo
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Resource with id=" + id + " not found"));
        return mapper.toDto(obj);
    }

    @Transactional
    public void delete(int id) {
        var obj = repo
            .findById(id)
            .orElseThrow(() -> new ValidationException("Resource with id=" + id + " not found"));
        var currentUser = userService.getCurrentUser();

        if (obj.getOwner() == currentUser || currentUser.isAdmin()) {
            repo.deleteById(id);
        } else {
            throw new PermissionDeniedException("You can't delete this resource");
        }
    }
}