package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.dto.CompilationAccept;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation toEntity(CompilationAccept compilationAccept);

    @Mapping(target = "events", source = "events")
    CompilationDto toDto(Compilation compilation);

}
