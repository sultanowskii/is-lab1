package com.lab1.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab1.common.CRUDService;
import com.lab1.locations.dto.LocationCreateDto;
import com.lab1.locations.dto.LocationDto;
import com.lab1.users.UserService;

@Service
public class LocationService extends CRUDService<Location, LocationDto, LocationCreateDto> {
    @Autowired
    public LocationService(UserService userService, LocationRepository locationRepository, LocationMapper locationMapper) {
        super(userService, locationRepository, locationMapper);
    }
}
