package com.lab1.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab1.common.CRUDService;
import com.lab1.users.UserService;

@Service
public class LocationService extends CRUDService<Location> {
    @Autowired
    public LocationService(UserService userService, LocationRepository locationRepository) {
        super(userService, locationRepository);
    }
}
