package com.lab1.locations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.retry.annotation.*;

import com.lab1.common.CRUDService;
import com.lab1.common.error.ValidationException;
import com.lab1.locations.dto.LocationCreateDto;
import com.lab1.locations.dto.LocationDto;
import com.lab1.users.UserService;

@Service
public class LocationService extends CRUDService<Location, LocationDto, LocationCreateDto> {
    @Autowired
    public LocationService(UserService userService, LocationRepository locationRepository, LocationMapper locationMapper) {
        super(userService, locationRepository, locationMapper);
    }

    private Optional<Location> locationWithName(String name) {
        Specification<Location> specification = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("name"), name);

        return repo.findOne(specification);
    }

    @Override
    @Retryable(
        retryFor = { CannotAcquireLockException.class },
        notRecoverable = { ValidationException.class },
        maxAttempts = 20,
        backoff = @Backoff(delay = 50)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public LocationDto create(LocationCreateDto form) {
        String name = form.getName();
        if (locationWithName(name).isPresent()) {
            throw new ValidationException("Location with name=" + name + " already exists");
        }

        return super.create(form);
    }

    @Override
    @Retryable(
        retryFor = { CannotAcquireLockException.class },
        notRecoverable = { ValidationException.class },
        maxAttempts = 20,
        backoff = @Backoff(delay = 50)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public LocationDto update(int id, LocationCreateDto form) {
        String name = form.getName();
        var maybeLocation = locationWithName(name);
        if (maybeLocation.isPresent() && maybeLocation.get().getId() != id) {
            throw new ValidationException("Location with name=" + name + " already exists");
        }

        return super.update(id, form);
    }

    @Recover
    public LocationDto handleCreateCannotAcquireLockException(CannotAcquireLockException e, LocationCreateDto form) {
        throw new ValidationException("Could not acquire lock for creating location: " + form.getName());
    }

    @Recover
    public LocationDto handleUpdateCannotAcquireLockException(CannotAcquireLockException e, int id, LocationCreateDto form) {
        throw new ValidationException("Could not acquire lock for updating location: " + form.getName());
    }
}
