package ru.practicum.priv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentAccept;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<EventResponse> postComment(@PathVariable Long userId,
                                                     @PathVariable Long eventId,
                                                     @RequestBody @Valid CommentAccept commentAccept) {
        return commentService.postComment(userId, eventId, commentAccept);
    }

    @PatchMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<EventResponse> patchComment(@PathVariable @Positive Long userId,
                                                      @PathVariable @Positive Long eventId,
                                                      @PathVariable @Positive Long commentId,
                                                      @RequestBody @Valid CommentAccept commentAccept) {
        return commentService.patchComment(commentAccept, eventId, userId, commentId);
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable @Positive Long userId,
                                                @PathVariable @Positive Long eventId,
                                                @PathVariable @Positive Long commentId) {
        return commentService.deleteComment(eventId, userId, commentId);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<Set<CommentDto>> getCommentsForEvent(@PathVariable @Positive Long userId,
                                                               @PathVariable @Positive Long eventId) {
        return commentService.getCommentsForEvent(eventId, userId);
    }

    @GetMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<CommentDto> findCommentId(@PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId,
                                                    @PathVariable @Positive Long commentId) {
        return commentService.getCommentById(eventId, userId, commentId);
    }
}
