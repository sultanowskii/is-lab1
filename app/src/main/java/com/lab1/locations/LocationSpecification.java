package com.lab1.locations;

import org.springframework.stereotype.Component;

import com.lab1.common.CRUDSpecification;

@Component
public class LocationSpecification extends CRUDSpecification<Location> {
    @Override
    public boolean isFieldValid(String fieldName) {
        return fieldName.equals("name");
    }
}
