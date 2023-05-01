package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.dto.EventResponse;

import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private Long id;

    private String title;

    private Boolean pinned;

    private List<EventResponse> events;
}
