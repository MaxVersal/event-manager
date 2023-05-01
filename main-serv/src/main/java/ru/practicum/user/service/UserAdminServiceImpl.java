package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoAccept;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto createUser(UserDtoAccept userDtoAccept) {
        final User user = mapper.toEntity(userDtoAccept);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null) {
            return userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size))
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteUser(Long id) {
        final User currentUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id=%d was not found", id)));
        userRepository.deleteById(currentUser.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Успешное удаление");
    }
}
