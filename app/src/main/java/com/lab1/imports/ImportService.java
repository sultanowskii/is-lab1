package com.lab1.imports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.lab1.common.LoudValidator;
import com.lab1.common.TarGzUtil;
import com.lab1.common.error.ValidationException;
import com.lab1.imports.dto.StudyGroupImportDto;
import com.lab1.imports.dto.StudyGroupsImportDto;
import com.lab1.imports.dto.log.ImportLogCreateDto;
import com.lab1.locations.LocationService;
import com.lab1.persons.PersonService;
import com.lab1.studygroups.StudyGroupService;

import jakarta.validation.ConstraintViolationException;
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
    ImportLogService importLogService;
    @Autowired
    ImportMapper mapper;

    @Transactional
    public int createBulk(StudyGroupsImportDto dto) {
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
        var created = studyGroupService.createAll(studyGroupCreateDtos);

        return created.size();
    }

    private Yaml getYamlParser() {
        var loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false); // Пример настройки
        var constructor = new Constructor(StudyGroupsImportDto.class, loaderOptions);
        return new Yaml(constructor);
    }

    private void validateStudyGroupImportDto(StudyGroupImportDto dto) throws ConstraintViolationException {
        LoudValidator.validate(dto);
        LoudValidator.validate(dto.getGroupAdmin());
        LoudValidator.validate(dto.getGroupAdmin().getLocation());
    }

    private Path getOrCreateDataDir() {
        var path = Paths.get("./data");

        try {
            if (!Files.exists(path)) {
                path = Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private StudyGroupsImportDto extract(MultipartFile file, String fileKey) throws ConstraintViolationException, IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".tar.gz")) {
            throw new ValidationException(".tar.gz archive is expected");
        }

        // Save archive to specific dir
        var archivesDir = getOrCreateDataDir();
        var tarGzFile = archivesDir.resolve(fileKey);
        Files.copy(file.getInputStream(), tarGzFile, StandardCopyOption.REPLACE_EXISTING);

        // Extract tar.gz
        var tempDir = Files.createTempDirectory("upload");
        var yamlFiles = TarGzUtil.extractYamlArchive(tarGzFile.toFile(), tempDir.toFile());

        // Parse files
        var yamlParser = getYamlParser();
        List<StudyGroupsImportDto> dtos = new ArrayList<>();
        for (File yamlFile : yamlFiles) {
            InputStream inputStream = Files.newInputStream(yamlFile.toPath());
            StudyGroupsImportDto dto = yamlParser.load(inputStream);
            dtos.add(dto);
        }
        var flattenedDtos = dtos.stream().flatMap(sgs -> sgs.getObjects().stream()).toList();

        // Validate parsed DTOs
        flattenedDtos.stream().forEach(this::validateStudyGroupImportDto);

        // Remove tmp dir
        TarGzUtil.deleteDirectory(tempDir.toFile());

        return new StudyGroupsImportDto(flattenedDtos);
    }

    public void extractAndCreate(MultipartFile file) throws ConstraintViolationException, IOException {
        var fileKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        var logDto = new ImportLogCreateDto();
        logDto.setFileKey(fileKey);

        int created;
        try {
            var dtos = extract(file, fileKey);
            created = createBulk(dtos);
        } catch (Exception e) {
            logDto.setCreatedCount(null);
            logDto.setStatus(Status.FAIL);
            importLogService.create(logDto);
            throw e;
        }

        logDto.setCreatedCount(created);
        logDto.setStatus(Status.SUCCESS);
        importLogService.create(logDto);
    }
}
