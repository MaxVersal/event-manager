package ru.practicum.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoAccept;

import javax.transaction.Transactional;
import java.util.List;

public interface UserAdminService {
    UserDto createUser(UserDtoAccept userDtoAccept);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    ResponseEntity<Object> deleteUser(Long id);
}
