package ru.practicum.event.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.event.dto.EventAccept;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.dto.EventUpdateForUser;
import ru.practicum.event.dto.UpdateEventForAdmin;
import ru.practicum.event.model.State;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    ResponseEntity<EventResponse> postEvent(Long userId, EventAccept eventAccept);


    List<EventResponse> getEventsForUser(Long userId, Integer from, Integer size);

    ResponseEntity<EventResponse> findById(Long userId, Long eventId);

    ResponseEntity<EventResponse> updateEvent(EventUpdateForUser eventUpdateForUser, Long userId, Long eventId);

    ResponseEntity<List<EventResponse>> getEventsForAdmin(List<Long> users,
                                                          List<State> states,
                                                          List<Long> categories,
                                                          String rangeStart,
                                                          String rangeEnd,
                                                          Integer from,
                                                          Integer size);


    ResponseEntity<EventResponse> updateAdmEvent(UpdateEventForAdmin eventAdminRequest, Long id);


    ResponseEntity<List<EventResponse>> getPublicEvents(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        String rangeStart,
                                                        String rangeEnd,
                                                        Boolean onlyAvailable,
                                                        String sort,
                                                        Integer from,
                                                        Integer size,
                                                        HttpServletRequest request);


    ResponseEntity<EventResponse> getEventById(Long eventId, HttpServletRequest request);
}
