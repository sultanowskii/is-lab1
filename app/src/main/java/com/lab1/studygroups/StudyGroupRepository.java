package com.lab1.studygroups;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Integer> {

}
