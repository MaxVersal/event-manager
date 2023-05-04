package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentAccept;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    private final EventMapper eventMapper;

    @Override
    @Transactional
    public ResponseEntity<EventResponse> postComment(Long userId, Long eventId, CommentAccept commentAccept) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d not found", userId))
        );
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d not found", eventId))
        );
        Comment comment = commentMapper.toEntity(commentAccept);
        comment.setUser(user);
        commentRepository.save(comment);
        event.getComments().add(comment);
        eventRepository.save(event);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventMapper.toEventResponse(event));
    }
}
