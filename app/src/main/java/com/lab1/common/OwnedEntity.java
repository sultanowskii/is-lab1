package com.lab1.common;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class OwnedEntity extends Owned implements Entity {
    
}
