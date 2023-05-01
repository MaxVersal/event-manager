package ru.practicum.event.service;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventAccept;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.dto.EventUpdateForUser;
import ru.practicum.event.dto.UpdateEventForAdmin;
import ru.practicum.event.model.State;

import java.util.List;

public interface EventService {
    @Transactional
    ResponseEntity<EventResponse> postEvent(Long userId, EventAccept eventAccept);

    @Transactional
    List<EventResponse> getEventsForUser(Long userId, Integer from, Integer size);

    @Transactional
    ResponseEntity<EventResponse> findById(Long userId, Long eventId);

    @Transactional
    ResponseEntity<EventResponse> updateEvent(EventUpdateForUser eventUpdateForUser, Long userId, Long eventId);

    @Transactional
    ResponseEntity<List<EventResponse>> getEventsForAdmin(List<Long> users,
                                                          List<State> states,
                                                          List<Long> categories,
                                                          String rangeStart,
                                                          String rangeEnd,
                                                          Integer from,
                                                          Integer size);

    @Transactional
    ResponseEntity<EventResponse> updateAdmEvent(UpdateEventForAdmin eventAdminRequest, Long id);

    @Transactional
    ResponseEntity<List<EventResponse>> getPublicEvents(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        String rangeStart,
                                                        String rangeEnd,
                                                        Boolean onlyAvailable,
                                                        String sort,
                                                        Integer from,
                                                        Integer size);

    @Transactional
    ResponseEntity<EventResponse> getEventById(Long eventId);
}
