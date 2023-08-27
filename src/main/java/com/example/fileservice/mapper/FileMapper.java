package com.example.fileservice.mapper;


import com.example.fileservice.dto.FileDto;
import com.example.fileservice.module.File;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper(componentModel = "spring")
public abstract class FileMapper {
    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract File toEntity(FileDto dto);

    public abstract FileDto toDto(File file);

    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract File updateFilesFromDto(FileDto dto, @MappingTarget File file);

    public abstract Set<FileDto> toSetDto(Set<File> file);

}
