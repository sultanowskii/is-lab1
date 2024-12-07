package com.lab1.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lab1.common.CRUDController;
import com.lab1.locations.dto.*;
import com.lab1.users.UserService;

@RestController
@RequestMapping("/api/locations")
public class LocationController extends CRUDController<Location, LocationDto, LocationCreateDto> {
    @Autowired
    public LocationController(LocationService locationService, LocationSpecification locationSpecification, UserService userService) {
        super(locationService, locationSpecification, userService);
    }
}

