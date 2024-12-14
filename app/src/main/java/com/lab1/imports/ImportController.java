package com.lab1.imports;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.ValidationException;
import com.lab1.common.paging.PaginationMapper;
import com.lab1.common.paging.dto.PaginationDto;
import com.lab1.imports.dto.StudyGroupsImportDto;
import com.lab1.imports.dto.log.ImportLogDto;
import com.lab1.users.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/imports")
public class ImportController {
    @Autowired
    private final ImportLogService importLogService;
    @Autowired
    private ImportService importService;
    @Autowired
    private ImportLogSpecification specBuilder;
    @Autowired
    private final UserService userService;
    @Autowired
    private PaginationMapper paginationMapper;

    @GetMapping
    @Operation(summary = "Get all imports", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Page<ImportLogDto>> getAll(SearchParamsDto searchParamsDto, @PageableDefault(size = 20) PaginationDto pagiationDto) throws ValidationException {
        searchParamsDto.validate();
        var user = userService.getCurrentUser();
        Specification<ImportLog> spec = specBuilder.build(user);
        if (!searchParamsDto.isEmpty()) {
            spec = specBuilder.buildWithSearchParams(user, searchParamsDto);
        }
        final var paginator = paginationMapper.toEntity(pagiationDto);
        return ResponseEntity.ok().body(importLogService.getAll(spec, paginator));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) {
        StudyGroupsImportDto dto;
        try {
            dto = importService.extractAndCreate(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        importService.createBulk(dto);

        return ResponseEntity.status(201).build();
    }
}
