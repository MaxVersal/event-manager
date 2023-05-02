package ru.practicum.request.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ResponseEntity<ParticipationRequestDto> postRequest(Long eventId, Long userId);

    ResponseEntity<List<ParticipationRequestDto>> findRequestsByUserId(Long userId);

    ResponseEntity<ParticipationRequestDto> update(Long userId, Long requestId);

    ResponseEntity<List<ParticipationRequestDto>> findByUserIdAndEventId(Long eventId, Long userId);

    EventRequestStatusUpdateResult confirmRequests(EventRequestStatusUpdateRequest statusRequest,
                                                                   Long eventId,
                                                                   Long userId);
}
