package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
