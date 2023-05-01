package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoAccept;
import ru.practicum.user.dto.UserForEvent;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDtoAccept user);

    UserForEvent toUserEvent(User user);
}
