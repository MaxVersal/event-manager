package ru.practicum.event.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.UserForEvent;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EventResponse {
    Long id;

    String annotation;

    Category category;

    Integer confirmedRequests;

    String createdOn;

    String description;

    String publishedOn;

    String eventDate;

    UserForEvent initiator;

    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    State state;

    String title;

    Integer views;
}
