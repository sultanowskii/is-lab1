package com.lab1.studygroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/studyGroups")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    // Создание нового StudyGroup
    @PostMapping
    public ResponseEntity<StudyGroup> createStudyGroup(@Valid @RequestBody StudyGroup studyGroup) {
        StudyGroup savedStudyGroup = studyGroupService.saveStudyGroup(studyGroup);
        return ResponseEntity.status(201).body(savedStudyGroup);  // HTTP статус 201 (Created)
    }

    // Получение всех StudyGroup
    @GetMapping
    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupService.getAllStudyGroups();
    }

    // Получение StudyGroup по ID
    @GetMapping("/{id}")
    public ResponseEntity<StudyGroup> getStudyGroupById(@PathVariable("id") int id) {
        Optional<StudyGroup> studyGroup = studyGroupService.getStudyGroupById(id);
        return studyGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Обновление StudyGroup по ID
    @PutMapping("/{id}")
    public ResponseEntity<StudyGroup> updateStudyGroup(@PathVariable("id") int id, @Valid @RequestBody StudyGroup studyGroupDetails) {
        Optional<StudyGroup> existingStudyGroup = studyGroupService.getStudyGroupById(id);
        if (existingStudyGroup.isPresent()) {
            studyGroupDetails.setId(id);  // Устанавливаем ID, чтобы обновить существующую запись
            StudyGroup updatedStudyGroup = studyGroupService.saveStudyGroup(studyGroupDetails);
            return ResponseEntity.ok(updatedStudyGroup);
        } else {
            return ResponseEntity.notFound().build();  // Если StudyGroup не найден, возвращаем 404
        }
    }

    // Удаление StudyGroup по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyGroup(@PathVariable("id") int id) {
        Optional<StudyGroup> existingStudyGroup = studyGroupService.getStudyGroupById(id);
        if (existingStudyGroup.isPresent()) {
            studyGroupService.deleteStudyGroup(id);
            return ResponseEntity.noContent().build();  // HTTP статус 204 (No Content)
        } else {
            return ResponseEntity.notFound().build();  // Если StudyGroup не найден, возвращаем 404
        }
    }
}

