package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {

//    @Mapping(source = "app", target = "app")
//    @Mapping(source = "uri", target = "uri")
//    @Mapping(source = "ip", target = "ip")
//    @Mapping(source = "timestamp", target = "timestamp")
    EndpointHitDto toDto(Hit hit);

//    @Mapping(source = "app", target = "app")
//    @Mapping(source = "uri", target = "uri")
//    @Mapping(source = "ip", target = "ip")
//    @Mapping(source = "timestamp", target = "timestamp")
    Hit toEntity(EndpointHitDto dto);

//    @Mapping(target = "app", source = "app")
//    @Mapping(target = "uri", source = "uri")
    ViewStats toStat(Hit hit);
}
