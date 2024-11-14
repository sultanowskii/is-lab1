package com.lab1.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CRUDRepository<T extends OwnedEntity> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {
}
