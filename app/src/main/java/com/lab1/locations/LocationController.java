package com.lab1.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Создание нового Location
    @PostMapping
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        Location savedLocation = locationService.saveLocation(location);
        return ResponseEntity.status(201).body(savedLocation);  // HTTP статус 201 (Created)
    }

    // Получение всех Location
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    // Получение Location по ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable("id") int id) {
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Обновление Location по ID
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") int id, @Valid @RequestBody Location locationDetails) {
        Optional<Location> existingLocation = locationService.getLocationById(id);
        if (existingLocation.isPresent()) {
            locationDetails.setId(id);  // Устанавливаем ID, чтобы обновить существующую запись
            Location updatedLocation = locationService.saveLocation(locationDetails);
            return ResponseEntity.ok(updatedLocation);
        } else {
            return ResponseEntity.notFound().build();  // Если Location не найден, возвращаем 404
        }
    }

    // Удаление Location по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") int id) {
        Optional<Location> existingLocation = locationService.getLocationById(id);
        if (existingLocation.isPresent()) {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();  // HTTP статус 204 (No Content)
        } else {
            return ResponseEntity.notFound().build();  // Если Location не найден, возвращаем 404
        }
    }
}

