package com.lab1.extra.studygroups;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab1.extra.studygroups.dto.StudyGroupChangeFormOfEducationToRequestDto;
import com.lab1.extra.studygroups.dto.StudyGroupCountTotalExpelledStudentsResponseDto;
import com.lab1.extra.studygroups.dto.StudyGroupCountWithAverageMarkResponseDto;
import com.lab1.extra.studygroups.dto.StudyGroupDeleteWithAverageMarkRequestDto;
import com.lab1.studygroups.dto.StudyGroupDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/extra/study-groups")
@AllArgsConstructor
public class StudyGroupExtraController {
    private final StudyGroupExtraService studyGroupExtraService;

    @PostMapping("/delete-with-average-mark")
    @Operation(summary = "Delete all study groups with specified averageMark", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<Void> deleteWithAverageMark(@RequestBody StudyGroupDeleteWithAverageMarkRequestDto dto) {
        studyGroupExtraService.deleteWithAverageMark(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-with-average-mark")
    @Operation(summary = "Count study groups with specified averageMark", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<StudyGroupCountWithAverageMarkResponseDto> countWithAverageMark(@RequestParam(name = "averageMark") int averageMark) {
        final var result = studyGroupExtraService.countWithAverageMark(averageMark);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find-with-name-like")
    @Operation(summary = "Find study groups with name containing provided partialName", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<List<StudyGroupDto>> findWithNameLike(@RequestParam(name = "partialName") String partialName) {
        final var result = studyGroupExtraService.findWithNameLike(partialName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count-total-expelled-students")
    @Operation(summary = "Count a total number of expelled students in all study groups", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<StudyGroupCountTotalExpelledStudentsResponseDto> countTotalExpelledStudents() {
        final var result = studyGroupExtraService.countTotalExpelledStudents();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-form-of-education")
    @Operation(summary = "Change form of education of a study group", security = @SecurityRequirement(name = "bearerTokenAuth"))
    public ResponseEntity<StudyGroupDto> changeFormOfEducationTo(@RequestBody StudyGroupChangeFormOfEducationToRequestDto dto) {
        final var result = studyGroupExtraService.changeFormOfEducationTo(dto);
        return ResponseEntity.ok(result);
    }
}
