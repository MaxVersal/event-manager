package ru.practicum.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoAccept;

import javax.transaction.Transactional;
import java.util.List;

public interface UserAdminService {
    @Transactional
    UserDto createUser(UserDtoAccept userDtoAccept);

    @Transactional
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    @Transactional
    ResponseEntity<Object> deleteUser(Long id);
}
