package com.lab1.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab1.common.dto.SearchParamsDto;
import com.lab1.common.error.ValidationException;
import com.lab1.common.paging.PaginationMapper;
import com.lab1.common.paging.dto.PaginationDto;
import com.lab1.imports.dto.ImportDto;
import com.lab1.users.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/imports")
public class ImportController {
    @Autowired
    private final ImportService service;
    @Autowired
    private ImportSpecification specBuilder;
    @Autowired
    private final UserService userService;
    @Autowired
    private PaginationMapper paginationMapper;

    @GetMapping
    @Operation(summary = "Get all objects", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Page<ImportDto>> getAll(SearchParamsDto searchParamsDto, @PageableDefault(size = 20) PaginationDto pagiationDto) throws ValidationException {
        searchParamsDto.validate();
        var user = userService.getCurrentUser();
        Specification<Import> spec = specBuilder.build(user);
        if (!searchParamsDto.isEmpty()) {
            spec = specBuilder.buildWithSearchParams(user, searchParamsDto);
        }
        final var paginator = paginationMapper.toEntity(pagiationDto);
        return ResponseEntity.ok().body(service.getAll(spec, paginator));
    }
}
