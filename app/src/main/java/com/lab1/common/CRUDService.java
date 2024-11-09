package com.lab1.common;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.users.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CRUDService<T extends OwnedEntity> implements com.lab1.common.Service<T> {
    private final UserService userService;
    private final JpaRepository<T, Integer> repo;

    @Transactional
    public T create(T obj) {
        obj.setOwner(userService.getCurrentUser());
        obj.setCreatedAt(ZonedDateTime.now());
        
        return repo.save(obj);
    }

    @Transactional
    public T update(int id, T updateObj) {
        var obj = repo.findById(id).orElseThrow(() -> new RuntimeException("Resource with id=" + id + " not found"));

        updateObj.setId(obj.getId());
        obj.setUpdatedBy(userService.getCurrentUser());
        obj.setUpdatedAt(ZonedDateTime.now());

        return repo.save(obj);
    }

    public Page<T> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<T> get(int id) {
        return repo.findById(id);
    }

    @Transactional
    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Resource with id=" + id + " not found");
        }

        repo.deleteById(id);
    }
}