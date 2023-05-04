package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "requester", expression = "java(request.getUser().getId())")
    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    @Mapping(target = "created", source = "created", qualifiedByName = "mapDate")
    ParticipationRequestDto toDto(Request request);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "user", ignore = true)
    Request toEntity(ParticipationRequestDto dto);

    @Named("mapDate")
    default String mapDates(LocalDateTime date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(pattern);
    }
}
