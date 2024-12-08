package com.lab1.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lab1.common.error.NotFoundException;
import com.lab1.common.paging.Paginator;
import com.lab1.common.paging.SmartPage;
import com.lab1.imports.dto.log.ImportLogCreateDto;
import com.lab1.imports.dto.log.ImportLogDto;
import com.lab1.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportLogService {
    @Autowired
    protected final UserService userService;
    private final ImportLogRepository repository;
    protected final ImportLogMapper mapper;

    public ImportLogDto create(ImportLogCreateDto createDto) {
        var obj = mapper.toEntityFromCreateDto(createDto);
        obj.setPerformer(userService.getCurrentUser());
        var createdObj = repository.save(obj);
        return mapper.toDto(createdObj);
    }

    public ImportLogDto get(int id) {
        var obj = repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Import with id=" + id + " not found"));
        return mapper.toDto(obj);
    }

    public Page<ImportLogDto> getAll(Specification<ImportLog> specification, Paginator paginator) {
        final var allObjects = repository.findAll(specification, paginator.getSort());
        final var paged = new SmartPage<>(allObjects, paginator);
        return paged.map(o -> mapper.toDto(o));
    }
}
