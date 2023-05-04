package ru.practicum.priv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestsConrtollerPrivate {

    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> postRequest(@PathVariable Long userId,
                                                               @RequestParam(value = "eventId") Long eventId) {
        return requestService.postRequest(eventId, userId);
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUser(@PathVariable Long userId) {
        return requestService.findRequestsByUserId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequest(@PathVariable Long userId,
                                                                 @PathVariable Long requestId) {
        return requestService.update(userId, requestId);
    }
}
