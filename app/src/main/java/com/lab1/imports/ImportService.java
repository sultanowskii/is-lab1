package com.lab1.imports;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.imports.dto.StudyGroupsImportDto;
import com.lab1.locations.LocationService;
import com.lab1.persons.PersonService;
import com.lab1.studygroups.StudyGroupService;

import lombok.*;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class ImportService {
    @Autowired
    LocationService locationService;
    @Autowired
    PersonService personService;
    @Autowired
    StudyGroupService studyGroupService;
    @Autowired
    ImportMapper mapper;

    @Transactional
    public void createBulk(StudyGroupsImportDto dto) {
        var studentGroupImportDtos = dto.getObjects();

        var locationImportDtos = studentGroupImportDtos.stream().map(sg -> sg.getGroupAdmin().getLocation()).toList();
        var createdLocations = locationService.createAll(
            locationImportDtos
                .stream()
                .map(l -> mapper.toLocationCreateDto(l))
                .toList()
        );

        var personCreateDtos = IntStream
        .range(0, createdLocations.size())
        .mapToObj(
            i -> {
                var personImportDto = studentGroupImportDtos.get(i).getGroupAdmin();

                var personCreateDto = mapper.toPersonCreateDto(personImportDto);
                personCreateDto.setLocationId(createdLocations.get(i).getId());

                return personCreateDto;
            }
        )
        .toList();
        var createdPersons = personService.createAll(personCreateDtos);

        var studyGroupCreateDtos = IntStream
        .range(0, createdPersons.size())
        .mapToObj(
            i -> {
                var studyGroupImportDto = studentGroupImportDtos.get(i);

                var studyGroupCreateDto = mapper.toStudyGroupCreateDto(studyGroupImportDto);
                studyGroupCreateDto.setGroupAdminId(createdPersons.get(i).getId());

                return studyGroupCreateDto;
            }
        )
        .toList();
        studyGroupService.createAll(studyGroupCreateDtos);
    }
}
