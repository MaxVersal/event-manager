package ru.practicum.request.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.transaction.Transactional;
import java.util.List;

public interface RequestService {
    @Transactional
    ResponseEntity<ParticipationRequestDto> postRequest(Long eventId, Long userId);

    @Transactional
    ResponseEntity<List<ParticipationRequestDto>> findRequestsByUserId(Long userId);

    @Transactional
    ResponseEntity<ParticipationRequestDto> update(Long userId, Long requestId);

    @Transactional
    ResponseEntity<List<ParticipationRequestDto>> findByUserIdAndEventId(Long eventId, Long userId);

    @Transactional
    EventRequestStatusUpdateResult confirmRequests(EventRequestStatusUpdateRequest statusRequest,
                                                                   Long eventId,
                                                                   Long userId);
}
