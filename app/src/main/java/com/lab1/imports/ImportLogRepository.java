package com.lab1.imports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportLogRepository extends JpaRepository<ImportLog, Integer>, JpaSpecificationExecutor<ImportLog> {
}
