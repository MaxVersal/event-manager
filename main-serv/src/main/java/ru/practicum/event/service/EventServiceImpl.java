package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.dto.EventAccept;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.dto.EventUpdateForUser;
import ru.practicum.event.dto.UpdateEventForAdmin;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.EventDateException;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EventMapper eventMapper;

    private final LocationRepository locationRepository;

    private final StatsClient client;

    @Override
    @Transactional
    public ResponseEntity<EventResponse> postEvent(Long userId, EventAccept eventAccept) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found", userId)));

        Event event = eventMapper.toEntity(eventAccept);
        event = eventWrap(event, eventAccept, user);
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException(String.format("Event date is not valid! eventDate =%s", event.getEventDate()));
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventMapper.toEventResponse(eventRepository.save(event)));

    }

    @Override
    @Transactional
    public List<EventResponse> getEventsForUser(Long userId, Integer from, Integer size) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id = %d not found.", userId))
        );
        List<Event> events = eventRepository.findAllByInitiator(user, PageRequest.of(from / size, size));
        return events.stream().map(eventMapper::toEventResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseEntity<EventResponse> findById(Long userId, Long eventId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id = %d not found.", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found.", eventId))
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toEventResponse(eventRepository.findByEventIdAndUser(event.getId(), user.getId())));
    }

    @Override
    @Transactional
    public ResponseEntity<EventResponse> updateEvent(EventUpdateForUser eventUpdateForUser, Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id = %d not found.", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found.", eventId))
        );
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("The event has already been published! Event state=%s", event.getState()));
        }
        eventUpdate(event, eventUpdateForUser.getTitle(), eventUpdateForUser.getAnnotation(), eventUpdateForUser.getDescription());
        Optional.ofNullable(eventUpdateForUser.getDescription()).ifPresent(ev -> {
            if (!StringUtils.isBlank(eventUpdateForUser.getDescription()))
                event.setDescription(eventUpdateForUser.getDescription());
        });
        Optional.ofNullable(eventUpdateForUser.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventUpdateForUser.getEventDate()).ifPresent(ev -> {
            final LocalDateTime date = LocalDateTime.parse(eventUpdateForUser.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (date.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new EventDateException(String.format("Event date is not valid! eventDate=%s", eventUpdateForUser.getEventDate()));
            }
            event.setEventDate(date);
        });
        Optional.ofNullable(eventUpdateForUser.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventUpdateForUser.getCategory()).ifPresent(ev -> {
            final Category category = categoryRepository.findById(eventUpdateForUser.getCategory()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("Category with id=%d not found.", eventUpdateForUser.getCategory()))
            );
            event.setCategory(category);
        });
        Optional.ofNullable(eventUpdateForUser.getLocation()).ifPresent(ev -> {
            Location location = locationRepository.save(eventMapper.toLocEntity(eventUpdateForUser.getLocation()));
            event.setLocation(location);
        });
        if (eventUpdateForUser.getStateAction() != null) {
            final StateAction stateAction = StateAction.valueOf(eventUpdateForUser.getStateAction());
            switch (stateAction) {
                case SEND_TO_REVIEW: {
                    event.setState(State.PENDING);
                    break;
                }
                case CANCEL_REVIEW: {
                    event.setState(State.CANCELED);
                    break;
                }
                default:
                    throw new EntityNotFoundException(String.format("Incorrect stateAction=%s", stateAction));
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toEventResponse(event));

    }

    @Override
    @Transactional
    public ResponseEntity<List<EventResponse>> getEventsForAdmin(List<Long> users,
                                                                 List<State> states,
                                                                 List<Long> categories,
                                                                 String rangeStart,
                                                                 String rangeEnd,
                                                                 Integer from,
                                                                 Integer size) {
        final QEvent event = QEvent.event;
        final BooleanBuilder builder = new BooleanBuilder();
        if (users != null && !users.isEmpty()) {
            builder.and(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            builder.and(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            builder.and(event.category.id.in(categories));
        }
        if ((rangeStart != null && !rangeStart.isEmpty()) && (rangeEnd != null && !rangeEnd.isEmpty())) {
            final LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            final LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            builder.and(event.eventDate.between(start, end));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventRepository.findAll(builder, PageRequest.of(from / size, size))
                        .stream()
                        .map(eventMapper::toEventResponse)
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ResponseEntity<EventResponse> updateAdmEvent(UpdateEventForAdmin eventAdminRequest, Long id) {
        final Event event = eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", id))
        );
        eventUpdate(event, eventAdminRequest.getTitle(), eventAdminRequest.getAnnotation(), eventAdminRequest.getDescription());
        Optional.ofNullable(eventAdminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventAdminRequest.getEventDate()).ifPresent(it -> {
            final LocalDateTime eventDate = LocalDateTime.parse(eventAdminRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ConflictException(String.format("Event date is not valid! eventDate=%s", eventAdminRequest.getEventDate()));
            }
            event.setEventDate(eventDate);
        });
        Optional.ofNullable(eventAdminRequest.getCategory()).ifPresent(it -> {
            final Category categoryWrap = categoryRepository.findById(eventAdminRequest.getCategory()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("Category with id=%d was not found", eventAdminRequest.getCategory()))
            );
            event.setCategory(categoryWrap);
        });
        Optional.ofNullable(eventAdminRequest.getLocation()).ifPresent(it -> {
            final Location location = eventMapper.toLocEntity(eventAdminRequest.getLocation());
            final Location locationWrap = locationRepository.save(location);
            event.setLocation(locationWrap);
        });
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException(String.format("The event has already been published! Event state=%s", event.getState()));
        }
        if (eventAdminRequest.getStateAction() != null) {
            final StateAction stateAction = StateAction.valueOf(eventAdminRequest.getStateAction());
            switch (stateAction) {
                case PUBLISH_EVENT: {
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(State.PUBLISHED);
                    break;
                }
                case REJECT_EVENT: {
                    event.setState(State.CANCELED);
                    throw new ConflictException(String.format("You can't publish a canceled event! Event state=%s", event.getState()));
                }
                default:
                    throw new EntityNotFoundException(String.format("Incorrect event stateAction=%s", event.getState()));
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toEventResponse(eventRepository.save(event)));
    }


    @Override
    @Transactional
    public ResponseEntity<List<EventResponse>> getPublicEvents(String text,
                                                               List<Long> categories,
                                                               Boolean paid,
                                                               String rangeStart,
                                                               String rangeEnd,
                                                               Boolean onlyAvailable,
                                                               String sort,
                                                               Integer from,
                                                               Integer size,
                                                               HttpServletRequest httpServletRequest) {
        final QEvent event = QEvent.event;
        final QRequest request = QRequest.request;
        final PageRequest pageRequest = PageRequest.of((from / size), size);
        final BooleanBuilder conditions = new BooleanBuilder();
        if (text != null && !text.isBlank()) {
            conditions.and(event.annotation.likeIgnoreCase(text).or(event.description.likeIgnoreCase(text)));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.and(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.and(event.paid.eq(paid));
        }
        if ((rangeStart != null && !rangeStart.isEmpty()) && (rangeEnd != null && !rangeEnd.isEmpty())) {
            final LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            final LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            conditions.and(event.eventDate.between(start, end));
        } else {
            conditions.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable != null && onlyAvailable) {
            final NumberExpression<Long> participantLimit = request.event.id.eq(event.id).and(request.status.eq(Status.CONFIRMED)).count();
            conditions.and(participantLimit.loe(event.participantLimit));
        }
        List<Event> events = eventRepository.findAll(conditions, pageRequest).getContent();
        client.save(new EndpointHitDto("ewn-service", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now())).subscribe();
        for (Event event1 : events) {
            event1.setViews(event1.getViews() + 1);
            eventRepository.save(event1);
        }
        if (sort != null) {
            if (sort.equals("VIEWS")) {
                events = events
                        .stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .collect(Collectors.toList());
            }
            if (sort.equals("EVENT_DATE")) {
                events = events
                        .stream()
                        .sorted(Comparator.comparing(Event::getEventDate).reversed())
                        .collect(Collectors.toList());
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(events.stream().map(eventMapper::toEventResponse).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ResponseEntity<EventResponse> getEventById(Long eventId, HttpServletRequest httpServletRequest) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId))
        );
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException(String.format("The state must have a state=%s", State.PUBLISHED.name()));
        }
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        client.save(new EndpointHitDto("ewm-service", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now())).subscribe();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toEventResponse(event));
    }

    private void eventUpdate(Event eventWrap, String title, String annotation, String description) {
        Optional.ofNullable(title).ifPresent(it -> {
            if (!StringUtils.isBlank(title)) eventWrap.setTitle(title);
        });
        Optional.ofNullable(annotation).ifPresent(it -> {
            if (!StringUtils.isBlank(annotation)) eventWrap.setAnnotation(annotation);
        });
        Optional.ofNullable(description).ifPresent(it -> {
            if (!StringUtils.isBlank(description)) eventWrap.setDescription(description);
        });
    }

    private Event eventWrap(Event event, EventAccept eventAccept, User user) {
        event.setCategory(categoryRepository.findById(eventAccept.getCategory()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Categpry with id=%d not found", eventAccept.getCategory()))
        ));
        event.setLocation(locationRepository.save(eventMapper.toLocEntity(eventAccept.getLocation())));
        event.setInitiator(user);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setViews(0);
        event.setConfirmedRequests(0);
        return event;
    }
}
