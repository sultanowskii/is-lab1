package com.lab1.studygroups;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.common.CRUDService;
import com.lab1.common.Cache;
import com.lab1.studygroups.dto.*;
import com.lab1.users.UserService;

import redis.clients.jedis.Jedis;

@Service
public class StudyGroupService extends CRUDService<StudyGroup, StudyGroupDto, StudyGroupCreateDto> {
    private final Jedis jedis;

    @Autowired
    public StudyGroupService(UserService userService, StudyGroupRepository studyGroupRepository, StudyGroupMapper studyGroupMapper, Jedis jedis) {
        super(userService, studyGroupRepository, studyGroupMapper);
        this.jedis = jedis;
    }

    @Override
    @Transactional
    public StudyGroupDto create(StudyGroupCreateDto form) {
        var studyGroup = mapper.toEntityFromCreateDto(form);

        studyGroup.setOwner(userService.getCurrentUser());
        studyGroup.setCreatedAt(ZonedDateTime.now());
        studyGroup.setCreationDate(LocalDateTime.now());

        var createdStudyGroup = repo.save(studyGroup);

        return mapper.toDto(createdStudyGroup);
    }

    @Override
    @Transactional
    public void delete(int id) {
        super.delete(id);
        jedis.del(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS);
    }

    @Override
    @Transactional
    public StudyGroupDto update(int id, StudyGroupCreateDto form) {
        final var result = super.update(id, form);
        jedis.del(Cache.CACHE_KEY_TOTAL_EXPELLED_STUDENTS);
        return result;
    }
}
