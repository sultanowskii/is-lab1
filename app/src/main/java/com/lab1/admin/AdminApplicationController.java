package com.lab1.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lab1.admin.dto.AdminApplicationCreateDto;
import com.lab1.admin.dto.AdminApplicationDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/admin/applications")
@AllArgsConstructor
public class AdminApplicationController {
    private final AdminApplicationService adminApplicationService;

    @PostMapping
    @Operation(summary = "Create an admin application", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<AdminApplicationDto> create(@Valid @RequestBody AdminApplicationCreateDto form) {
        return ResponseEntity.status(201).body(adminApplicationService.create(form));
    }

    @GetMapping
    @Operation(summary = "Get all admin applications", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Page<AdminApplicationDto>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(adminApplicationService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admin application by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<AdminApplicationDto> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(adminApplicationService.get(id));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve admin application by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<AdminApplicationDto> approve(@PathVariable("id") int id) {
        return ResponseEntity.ok(adminApplicationService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject admin application by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<AdminApplicationDto> reject(@PathVariable("id") int id) {
        return ResponseEntity.ok(adminApplicationService.reject(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admin application by ID", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        adminApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
