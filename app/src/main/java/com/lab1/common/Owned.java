package com.lab1.common;

import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.lab1.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Owned {
    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = ZonedDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
