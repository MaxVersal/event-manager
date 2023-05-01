package ru.practicum.pub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final StatsClient client;

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getPublicEvents(@RequestParam(required = false) String text,
                                                               @RequestParam(required = false) List<Long> categories,
                                                               @RequestParam(required = false) Boolean paid,
                                                               @RequestParam(required = false) String rangeStart,
                                                               @RequestParam(required = false) String rangeEnd,
                                                               @RequestParam(required = false) Boolean onlyAvailable,
                                                               @RequestParam(required = false) String sort,
                                                               @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                               @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                               HttpServletRequest request) {
        client.save(new EndpointHitDto("ewn-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now())).subscribe();
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId,
                                                      HttpServletRequest request) {
        client.save(new EndpointHitDto("ewn-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now())).subscribe();
        return eventService.getEventById(eventId);
    }
}
