package ru.practicum.comment.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.comment.dto.CommentAccept;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.dto.EventResponse;

import java.util.Set;

public interface CommentService {

    ResponseEntity<EventResponse> postComment(Long userId, Long eventId, CommentAccept commentAccept);

    ResponseEntity<EventResponse> patchComment(CommentAccept commentAccept, Long eventId, Long userId, Long commentId);

    ResponseEntity<Object> deleteComment(Long eventId, Long userId, Long commentId);

    ResponseEntity<Set<CommentDto>> getCommentsForEvent(Long eventId, Long userId);

    ResponseEntity<CommentDto> getCommentById(Long eventId, Long userId, Long commentId);
}
