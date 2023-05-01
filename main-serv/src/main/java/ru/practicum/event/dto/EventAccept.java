package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventAccept {

    @NotNull
    String annotation;

    @NotNull
    Long category;

    @NotNull
    String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    @NotNull
    Boolean paid;

    @NotNull
    Integer participantLimit;

    @NotNull
    Boolean requestModeration;

    @NotNull
    String title;
}
