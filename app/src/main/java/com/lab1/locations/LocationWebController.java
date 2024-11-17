package com.lab1.locations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LocationWebController {
    @GetMapping("/locations")
    public String pageLocations() {
        return "locations/locations";
    }

    @GetMapping("/locations/create")
    public String pageLocationCreate() {
        return "locations/location-create";
    }

    @GetMapping("/locations/{id}")
    public String pageLocation() {
        return "locations/location";
    }
}
