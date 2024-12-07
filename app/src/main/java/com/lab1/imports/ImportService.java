package com.lab1.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lab1.common.error.NotFoundException;
import com.lab1.common.paging.Paginator;
import com.lab1.common.paging.SmartPage;
import com.lab1.imports.dto.ImportCreateDto;
import com.lab1.imports.dto.ImportDto;
import com.lab1.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {
    @Autowired
    protected final UserService userService;
    private final ImportRepository repository;
    protected final ImportMapper mapper;

    public ImportDto create(ImportCreateDto createDto) {
        var obj = mapper.toEntityFromCreateDto(createDto);
        obj.setPerformer(userService.getCurrentUser());
        var createdObj = repository.save(obj);
        return mapper.toDto(createdObj);
    }

    public ImportDto get(int id) {
        var obj = repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Import with id=" + id + " not found"));
        return mapper.toDto(obj);
    }

    public Page<ImportDto> getAll(Specification<Import> specification, Paginator paginator) {
        final var allObjects = repository.findAll(specification, paginator.getSort());
        final var paged = new SmartPage<>(allObjects, paginator);
        return paged.map(o -> mapper.toDto(o));
    }
}
