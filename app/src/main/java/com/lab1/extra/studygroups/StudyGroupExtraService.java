package com.lab1.extra.studygroups;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.common.Cache;
import com.lab1.common.Redis;
import com.lab1.common.error.NotFoundException;
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
    private final Redis redis;

    @Transactional
    public boolean deleteWithAverageMark(StudyGroupDeleteWithAverageMarkRequestDto dto) {
        return studyGroupRepository.deleteWithAverageMark(dto.getAverageMark());
    }

    public StudyGroupCountWithAverageMarkResponseDto countWithAverageMark(int averageMark) {
        final var count = studyGroupRepository.countWithAverageMark(averageMark);
        return new StudyGroupCountWithAverageMarkResponseDto(count);
    }

    public List<StudyGroupDto> findWithNameLike(String partialName) {
        return studyGroupRepository
            .findWithNameLike(partialName)
            .stream()
            .map(o -> studyGroupMapper.toDto(o))
            .toList();   
    }

    public StudyGroupCountTotalExpelledStudentsResponseDto countTotalExpelledStudents() {
        int total = 0;

        var jedis = redis.getResource();
        if (jedis.exists(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS)) {
            final var raw = jedis.get(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS);
            total = Integer.parseInt(raw);
        } else {
            total = studyGroupRepository.countTotalExpelledStudents();
            jedis.set(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS, String.valueOf(total));
        }
        jedis.close();

        return new StudyGroupCountTotalExpelledStudentsResponseDto(total);
    }

    @Transactional
    public StudyGroupDto changeFormOfEducationTo(StudyGroupChangeFormOfEducationToRequestDto dto) throws NotFoundException {
        final var updatedStudyGroup = studyGroupRepository.changeFormOfEducationTo(dto.getId(), dto.getFormOfEducation().name());
        if (updatedStudyGroup == null) {
            throw new NotFoundException("Study group with id=" + dto.getId() + " not found");
        }
        return studyGroupMapper.toDto(updatedStudyGroup);
    } 
}
