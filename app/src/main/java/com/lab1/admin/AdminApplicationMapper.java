package com.lab1.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.lab1.admin.dto.AdminApplicationCreateDto;
import com.lab1.admin.dto.AdminApplicationDto;
import com.lab1.common.BaseMapper;
import com.lab1.users.User;
import com.lab1.users.UserRepository;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class AdminApplicationMapper implements BaseMapper<AdminApplication, AdminApplicationDto, AdminApplicationCreateDto> {
    @Autowired
    protected UserRepository userRepo;

    @Mapping(source = "dto.userId", qualifiedByName = "mapUser", target = "user")
    public abstract AdminApplication toEntity(AdminApplicationDto dto);
    @Mapping(source = "createDto.userId", qualifiedByName = "mapUser", target = "user")
    public abstract AdminApplication toEntityFromCreateDto(AdminApplicationCreateDto createDto);
    @Mapping(source = "entity.user", qualifiedByName = "mapUserId", target = "userId")
    public abstract AdminApplicationDto toDto(AdminApplication entity);

    @Mapping(source = "updateDto.userId", qualifiedByName = "mapUser", target = "user")
    public abstract void update(@MappingTarget AdminApplication entity, AdminApplicationCreateDto updateDto);

    @Named("mapUser") 
    protected User mapUser(int userId) {
        return userRepo.findById(userId).orElse(null);
    }

    @Named("mapUserId") 
    protected Integer mapUserId(User user) {
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}

