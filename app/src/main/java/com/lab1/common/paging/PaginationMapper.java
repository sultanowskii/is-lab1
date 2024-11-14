package com.lab1.common.paging;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.data.domain.Sort;

import com.lab1.common.paging.dto.PaginationDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class PaginationMapper {
    @Mapping(source = "dto.page", target = "pageNumber")
    @Mapping(source = "dto.size", target = "pageSize")
    @Mapping(source = "dto.sort", target = "sortParameters", qualifiedByName = "mapSortParameters")
    public abstract Paginator toEntity(PaginationDto dto);

    @Named("mapSortParameters")
    protected Sort mapSortParameters(List<String> fieldList) {
        if (fieldList == null) {
            return Sort.unsorted();
        }
        return Sort.by(Sort.Direction.DESC, fieldList.toArray(String[]::new));
    }
}
