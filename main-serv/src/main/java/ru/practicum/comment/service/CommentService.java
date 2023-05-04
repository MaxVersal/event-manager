package ru.practicum.comment.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.comment.dto.CommentAccept;
import ru.practicum.event.dto.EventResponse;

public interface CommentService {

    ResponseEntity<EventResponse> postComment(Long userId, Long eventId, CommentAccept commentAccept);
}
