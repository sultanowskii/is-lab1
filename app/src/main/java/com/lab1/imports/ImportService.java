package com.lab1.imports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.lab1.common.LoudValidator;
import com.lab1.common.TarGzUtil;
import com.lab1.common.error.NotFoundException;
import com.lab1.common.error.ValidationException;
import com.lab1.imports.dto.StudyGroupImportDto;
import com.lab1.imports.dto.StudyGroupsImportDto;
import com.lab1.imports.dto.log.ImportLogCreateDto;
import com.lab1.imports.transaction.Coordinator;
import com.lab1.imports.transaction.DBParticipant;
import com.lab1.imports.transaction.S3Participant;
import com.lab1.locations.LocationService;
import com.lab1.persons.PersonService;
import com.lab1.studygroups.StudyGroupService;

import jakarta.validation.ConstraintViolationException;
import lombok.*;
import software.amazon.awssdk.services.s3.model.InvalidObjectStateException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class ImportService {
    @Autowired
    private LocationService locationService;
    @Autowired
    private PersonService personService;
    @Autowired
    private StudyGroupService studyGroupService;
    @Autowired
    private ImportLogService importLogService;
    @Autowired
    private ImportMapper mapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private S3Service s3Service;

    @Transactional
    public int createBulk(StudyGroupsImportDto dto) {
        // throw new DataAccessResourceFailureException("testy test");
        var studentGroupImportDtos = dto.getObjects();

        var locationImportDtos = studentGroupImportDtos.stream().map(sg -> sg.getGroupAdmin().getLocation()).toList();
        var createdLocations = locationService.createAll(
                locationImportDtos
                        .stream()
                        .map(l -> mapper.toLocationCreateDto(l))
                        .toList());

        var personCreateDtos = IntStream
                .range(0, createdLocations.size())
                .mapToObj(
                        i -> {
                            var personImportDto = studentGroupImportDtos.get(i).getGroupAdmin();

                            var personCreateDto = mapper.toPersonCreateDto(personImportDto);
                            personCreateDto.setLocationId(createdLocations.get(i).getId());

                            return personCreateDto;
                        })
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
                        })
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

    private StudyGroupsImportDto extract(MultipartFile file, String fileKey)
            throws ConstraintViolationException, IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".tar.gz")) {
            throw new ValidationException(".tar.gz archive is expected");
        }

        // Extract tar.gz
        var tempDir = Files.createTempDirectory("upload");
        var tarGzFile = tempDir.resolve(fileKey);
        Files.copy(file.getInputStream(), tarGzFile, StandardCopyOption.REPLACE_EXISTING);

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
        FileUtil.deleteDirectory(tempDir.toFile());
        return new StudyGroupsImportDto(flattenedDtos);
    }

    public void extractAndCreate(MultipartFile file) throws Exception {
        var fileKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        var logDto = new ImportLogCreateDto();
        logDto.setFileKey(fileKey);

        var dtos = extract(file, fileKey);

        try {
            // Save archive to specific dir
            var tmpDir = Files.createTempDirectory("upload");
            var tarGzFile = tmpDir.resolve(fileKey);
            Files.copy(file.getInputStream(), tarGzFile, StandardCopyOption.REPLACE_EXISTING);

            var s3p = new S3Participant(s3Service, fileKey, tarGzFile);
            var dbp = new DBParticipant(() -> createBulk(dtos), transactionManager);

            var coordinator = new Coordinator(s3p, dbp);
            coordinator.perform();
        } catch (Exception e) {
            logDto.setCreatedCount(null);
            logDto.setStatus(Status.FAIL);
            importLogService.create(logDto);
            throw e;
        }

        logDto.setCreatedCount(dtos.getObjects().size());
        logDto.setStatus(Status.SUCCESS);
        importLogService.create(logDto);
    }

    public byte[] getImportFile(int id) {
        var importLog = importLogService.get(id);

        var key = importLog.getFileKey();

        try {
            return s3Service.downloadFile(key);
        } catch (NoSuchKeyException | InvalidObjectStateException e) {
            throw new NotFoundException("Associated import file is not available or doesn't exist");
        } catch (IOException e) {
            throw new NotFoundException("Associated import file can't be read. Try again later");
        }
    }
}
