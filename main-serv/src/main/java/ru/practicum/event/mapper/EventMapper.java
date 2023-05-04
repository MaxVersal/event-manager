package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.event.dto.EventAccept;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserForEvent;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface EventMapper {
    LocationDto toLocDto(Location location);

    Location toLocEntity(LocationDto locationDto);

    UserForEvent toUserEvent(User user);

    @Mapping(target = "category",ignore = true)
    Event toEntity(EventAccept eventAccept);

    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "mapDate")
    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "mapDate")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "comments", source = "comments")
    EventResponse toEventResponse(Event event);

    @Named("mapDate")
    default String mapDates(LocalDateTime date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(pattern);
    }
}
