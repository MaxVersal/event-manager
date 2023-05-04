package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoAccept;
import ru.practicum.user.service.UserAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdmUserController {

    @Autowired
    private final UserAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid UserDtoAccept userDtoAccept) {
        return service.createUser(userDtoAccept);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                  @RequestParam(value = "from", defaultValue = "0") Integer from,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return service.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") @Positive Long id) {
        return service.deleteUser(id);
    }
}
