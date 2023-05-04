package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.dto.UpdateEventForAdmin;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdmEventController {
    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getEvents(@RequestParam(value = "users", required = false, defaultValue = "-1") List<Long> users,
                                                         @RequestParam(value = "states", required = false)List<State> states,
                                                         @RequestParam(value = "categories", required = false) List<Long> categories,
                                                         @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                         @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                         @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return eventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> adminPatchEvent(@PathVariable Long eventId,
                                                         @RequestBody UpdateEventForAdmin updateEventForAdmin) {
        return eventService.updateAdmEvent(updateEventForAdmin, eventId);
    }
}
