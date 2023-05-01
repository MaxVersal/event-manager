package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ResponseEntity<ParticipationRequestDto> postRequest(Long eventId, Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found.", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found.", eventId))
        );
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException((String.format("Repeated request for participation is not allowed, userId=%d and eventId=%d", userId, eventId)));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("The initiator of the event cannot send a request to participate, userId=%d", userId));
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException(String.format("The state must have a state=%s", State.PUBLISHED));
        }
        Integer currentParticipantLimit = requestRepository.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, event.getId());
        if (Objects.equals(event.getParticipantLimit(), currentParticipantLimit)) {
            throw new ConflictException(String.format("Maximum number of participants %d", currentParticipantLimit));
        }
        Request request = new Request();
        request.setUser(user);
        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        request.setEvent(event);
        eventRepository.save(event);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestMapper.toDto(requestRepository.save(request)));
    }

    @Override
    @Transactional
    public ResponseEntity<List<ParticipationRequestDto>> findRequestsByUserId(Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found.", userId))
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        requestRepository.findAllByUserIs(user)
                                .stream()
                                .map(requestMapper::toDto)
                                .collect(Collectors.toList())
                );
    }

    @Override
    @Transactional
    public ResponseEntity<ParticipationRequestDto> update(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found.", userId))
        );
        final Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Request with id=%d not found.", requestId))
        );
        request.setStatus(Status.CANCELED);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(requestMapper.toDto(requestRepository.save(request)));
    }

    @Override
    @Transactional
    public ResponseEntity<List<ParticipationRequestDto>> findByUserIdAndEventId(Long eventId, Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found.", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found.", eventId))
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        requestRepository.findAllByEvent_IdAndEvent_Initiator_Id(eventId, userId)
                                .stream().map(requestMapper::toDto)
                                .collect(Collectors.toList())
                );
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult confirmRequests(EventRequestStatusUpdateRequest statusRequest,
                                                          Long eventId,
                                                          Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found.", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found.", eventId))
        );
        final Status status = Status.valueOf(statusRequest.getStatus());
        final List<Long> ids = statusRequest.getRequestIds();
        final List<Request> requests = requestRepository.findAllByIdIn(ids);
        if (requests.stream().allMatch(r -> r.getStatus().equals(Status.PENDING))) {
            for (Request request : requests) {
                final Integer participantCount = requestRepository.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, event.getId());
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                    request.setStatus(Status.CONFIRMED);
                    requestRepository.save(request);
                }
                if (event.getParticipantLimit() > participantCount) {
                    if (status == Status.CONFIRMED) {
                        request.setStatus(Status.CONFIRMED);
                        requestRepository.save(request);
                    }
                    if (status == Status.REJECTED) {
                        request.setStatus(Status.REJECTED);
                        requestRepository.save(request);
                    }
                    event.setConfirmedRequests(requestRepository.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, event.getId()));
                    eventRepository.save(event);
                } else {
                    throw new ConflictException(String.format("Maximum number of participants %d", participantCount));
                }
            }
        } else {
            throw new ConflictException(String.format("The status must have a status=%s", Status.PENDING));
        }
        final List<Request> confirmed = requests
                .stream()
                .filter(r -> r.getStatus() == Status.CONFIRMED)
                .collect(Collectors.toList());
        final List<Request> rejected = requests
                .stream()
                .filter(r -> r.getStatus() == Status.REJECTED)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(
                confirmed.stream()
                        .map(requestMapper::toDto)
                        .collect(Collectors.toList()),
                rejected.stream()
                        .map(requestMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}
