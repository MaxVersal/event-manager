package ru.practicum.priv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventsControllerPrivate {
    private final EventService service;

    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventResponse> postEvent(@PathVariable(name = "userId") @Positive Long userId,
                                                   @RequestBody @Valid EventAccept eventAccept) {
        return service.postEvent(userId, eventAccept);
    }

    @GetMapping("/{userId}/events")
    public List<EventResponse> getEventsForUser(@PathVariable(name = "userId") Long userId,
                                                                @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return service.getEventsForUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventResponse> getEventForUser(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId) {
        return service.findById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventResponse> patchEvent(@RequestBody EventUpdateForUser eventUpdateForUser,
                                                    @PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId) {
        return service.updateEvent(eventUpdateForUser, userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsForUser(@PathVariable @Positive Long userId,
                                                                            @PathVariable @Positive Long eventId) {
        return requestService.findByUserIdAndEventId(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult confirmRequests(@RequestBody @Valid EventRequestStatusUpdateRequest request,
                                                                          @PathVariable @Positive Long userId,
                                                                          @PathVariable @Positive Long eventId) {
        return requestService.confirmRequests(request, eventId, userId);
    }


}
