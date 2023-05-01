package ru.practicum.event.dto;

import lombok.Data;

@Data
public class UpdateEventForAdmin {
    private String title;
    private String annotation;
    private String description;
    private String eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
    private String stateAction;
    private LocationDto location;
    private Long category;
}
