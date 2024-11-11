package com.lab1.extra.studygroups;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.extra.studygroups.dto.StudyGroupChangeFormOfEducationToRequestDto;
import com.lab1.extra.studygroups.dto.StudyGroupCountTotalExpelledStudentsResponseDto;
import com.lab1.extra.studygroups.dto.StudyGroupCountWithAverageMarkResponseDto;
import com.lab1.extra.studygroups.dto.StudyGroupDeleteWithAverageMarkRequestDto;
import com.lab1.studygroups.StudyGroupMapper;
import com.lab1.studygroups.StudyGroupRepository;
import com.lab1.studygroups.dto.StudyGroupDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudyGroupExtraService {
    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupMapper studyGroupMapper;

    @Transactional
    public boolean deleteWithAverageMark(StudyGroupDeleteWithAverageMarkRequestDto dto) {
        return studyGroupRepository.deleteWithAverageMark(dto.getAverageMark());
    }

    public StudyGroupCountWithAverageMarkResponseDto countWithAverageMark(int averageMark) {
        final var count = studyGroupRepository.countWithAverageMark(averageMark);
        return new StudyGroupCountWithAverageMarkResponseDto(count);
    }

    public List<StudyGroupDto> findWithNameLike(String partialName) {
        var aboba = studyGroupRepository.findWithNameLike(partialName);

        return aboba
            .stream()
            .map(o -> studyGroupMapper.toDto(o))
            .toList();   
    }

    public StudyGroupCountTotalExpelledStudentsResponseDto countTotalExpelledStudents() {
        final var total = studyGroupRepository.countTotalExpelledStudents();
        return new StudyGroupCountTotalExpelledStudentsResponseDto(total);
    }

    @Transactional
    public boolean changeFormOfEducationTo(StudyGroupChangeFormOfEducationToRequestDto dto) {
        return studyGroupRepository.changeFormOfEducationTo(dto.getId(), dto.getFormOfEducation());
    } 
}
