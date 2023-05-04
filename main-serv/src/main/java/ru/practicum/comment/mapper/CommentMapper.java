package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.dto.CommentAccept;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "userName", expression = "java(comment.getUser().getName())")
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentAccept commentAccept);
}
