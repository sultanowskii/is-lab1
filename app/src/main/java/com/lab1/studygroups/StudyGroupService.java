package com.lab1.studygroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudyGroupService implements com.lab1.common.Service<StudyGroup> {
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    public StudyGroup save(StudyGroup person) {
        return studyGroupRepository.save(person);
    }

    public Page<StudyGroup> getAll(Pageable pageable) {
        return studyGroupRepository.findAll(pageable);
    }

    public Optional<StudyGroup> get(Integer id) {
        return studyGroupRepository.findById(id);
    }

    public void delete(Integer id) {
        studyGroupRepository.deleteById(id);
    }
}
