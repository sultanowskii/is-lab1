package com.lab1.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminApplicationRepository extends JpaRepository<AdminApplication, Integer>, JpaSpecificationExecutor<AdminApplication> {

}
