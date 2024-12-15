package com.lab1.imports;

import com.lab1.users.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportLog implements com.lab1.common.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_log_seq")
    @SequenceGenerator(name = "import_log_seq", sequenceName = "import_log_seq", allocationSize = 1)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "performer", nullable = false)
    private User performer;

    @Column(name = "file_key", nullable = false)
    private String fileKey;

    @Column(name = "created_count", nullable = true)
    private Integer createdCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
