package com.lab1.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.admin.dto.AdminApplicationCreateDto;
import com.lab1.admin.dto.AdminApplicationDto;
import com.lab1.common.error.ValidationException;
import com.lab1.common.paging.Paginator;
import com.lab1.common.paging.SmartPage;
import com.lab1.common.error.PermissionDeniedException;
import com.lab1.users.UserService;
import com.lab1.users.UserType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminApplicationService {
    private final AdminApplicationRepository adminApplicationRepo;
    private final UserService userService;
    private final AdminApplicationMapper adminApplicationMapper;

    @Transactional
    public AdminApplicationDto create(AdminApplicationCreateDto form) {
        var application = adminApplicationMapper.toEntityFromCreateDto(form); 
        application.setStatus(AdminApplicationStatus.CREATED);
        var createdApplication = adminApplicationRepo.save(application);
        return adminApplicationMapper.toDto(createdApplication);
    }

    @Transactional
    private AdminApplication updateStatus(int id, AdminApplicationStatus status) {
        checkIfAdmin();

        var application = adminApplicationRepo
            .findById(id)
            .orElseThrow(() -> new ValidationException("Resource with id=" + id + " not found"));
        application.setStatus(status);
        var savedApplication = adminApplicationRepo.save(application);

        return savedApplication;
    }

    @Transactional
    public AdminApplicationDto approve(int id) {
        var application = updateStatus(id, AdminApplicationStatus.APPROVED);

        var user = application.getUser();
        user.setType(UserType.ADMIN);
        userService.save(user);

        return adminApplicationMapper.toDto(application);
    }

    public AdminApplicationDto reject(int id) {
        var updatedApplication = updateStatus(id, AdminApplicationStatus.REJECTED);
        return adminApplicationMapper.toDto(updatedApplication);
    }

    public Page<AdminApplicationDto> getAll(Specification<AdminApplication> specification, Paginator paginator) {
        checkIfAdmin();
        final var all = adminApplicationRepo.findAll(specification);
        final var paged = new SmartPage<>(all, paginator);
        return paged.map(o -> adminApplicationMapper.toDto(o));
    }

    public AdminApplicationDto get(int id) {
        var application = adminApplicationRepo
            .findById(id)
            .orElseThrow(() -> new ValidationException("Resource with id=" + id + " not found"));
        checkIfAdminOrOwner(application);
        return adminApplicationMapper.toDto(application);
    }

    @Transactional
    public void delete(int id) {
        var application = adminApplicationRepo
            .findById(id)
            .orElseThrow(() -> new ValidationException("Resource with id=" + id + " not found"));
        checkIfAdminOrOwner(application);
        adminApplicationRepo.deleteById(id);
    }

    private void checkIfAdminOrOwner(AdminApplication application) {
        var currentUser = userService.getCurrentUser();
        if (application.getUser().equals(currentUser) || currentUser.isAdmin()) {
            return;
        }
        throw new PermissionDeniedException("You don't have permissions to access this resource");
    }

    private void checkIfAdmin() {
        var currentUser = userService.getCurrentUser();
        if (!currentUser.isAdmin()) {
            throw new PermissionDeniedException("You don't have permissions to access this resource");
        }
    }
}
