package com.lab1.common;

import java.time.ZonedDateTime;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.common.error.BadRequestException;
import com.lab1.common.error.PermissionDeniedException;
import com.lab1.users.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CRUDService<T extends OwnedEntity, TDto, TCreateDto> implements com.lab1.common.Service<T, TDto, TCreateDto> {
    private final UserService userService;
    private final CRUDRepository<T> repo;
    private final BaseMapper<T, TDto, TCreateDto> mapper;

    @Transactional
    public TDto create(TCreateDto form) {
        var obj = mapper.toEntityFromCreateDto(form);

        obj.setOwner(userService.getCurrentUser());
        obj.setCreatedAt(ZonedDateTime.now());
        
        var createdObj = repo.save(obj);

        return mapper.toDto(createdObj);
    }

    @Transactional
    public TDto update(int id, TCreateDto form) {
        var obj = repo.findById(id).orElseThrow(() -> new BadRequestException("Resource with id=" + id + " not found"));

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

    public Page<TDto> getAll(Specification<T> specification, Pageable pageable) {
        return repo.findAll(specification, pageable).map(o -> mapper.toDto(o));
    }

    public TDto get(int id) {
        var obj = repo.findById(id).orElseThrow(() -> new BadRequestException("Resource with id=" + id + " not found"));
        return mapper.toDto(obj);
    }

    @Transactional
    public void delete(int id) {
        var obj = repo.findById(id).orElseThrow(() -> new BadRequestException("Resource with id=" + id + " not found"));
        var currentUser = userService.getCurrentUser();

        if (obj.getOwner() == currentUser || currentUser.isAdmin()) {
            repo.deleteById(id);
        } else {
            throw new PermissionDeniedException("You can't delete this resource");
        }
    }
}