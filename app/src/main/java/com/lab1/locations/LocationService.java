package com.lab1.locations;

import java.util.List;
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

        var results = repo.findAll(specification);

        var size = results.size();

        if (size == 0) {
            return Optional.empty();
        } else if (size == 1) {
            return Optional.of(results.get(0));
        } else {
            throw new ValidationException("Location with name=" + name + " already exists");
        }
    }

    @Override
    @Retryable(
        retryFor = { CannotAcquireLockException.class },
        notRecoverable = { ValidationException.class },
        maxAttempts = 20,
        backoff = @Backoff(delay = 50)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<LocationDto> createAll(List<LocationCreateDto> forms) {
        for (var form : forms) {
            var name = form.getName();

            var sameName = forms.stream().filter(f -> f.getName() == name).toList();
            if (sameName.size() > 1) {
                throw new ValidationException("Location with name=" + name + " is present multiple times in request!");
            }

            if (locationWithName(name).isPresent()) {
                throw new ValidationException("Location with name=" + name + " already exists");
            }
        }
        return super.createAll(forms);
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

    @Recover
    public LocationDto handleCreateAllCannotAcquireLockException(CannotAcquireLockException e, List<LocationCreateDto> forms) {
        throw new ValidationException("Could not acquire lock for updating locations");
    }
}
