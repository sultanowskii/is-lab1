package com.lab1.studygroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    public StudyGroup saveStudyGroup(StudyGroup person) {
        return studyGroupRepository.save(person);
    }

    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupRepository.findAll();
    }

    public Optional<StudyGroup> getStudyGroupById(Integer id) {
        return studyGroupRepository.findById(id);
    }

    public void deleteStudyGroup(Integer id) {
        studyGroupRepository.deleteById(id);
    }
}
