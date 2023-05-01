package ru.practicum.pub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
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
        log.info("GET request to /events");
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId,
                                                      HttpServletRequest request) {
        log.info("GET request for /events/" + eventId);
        return eventService.getEventById(eventId, request);
    }
}
