package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);

        return client.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @PathVariable Long userId) {
        log.info("Update user {}, userId={}", userDto, userId);

        return client.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Get user, userId={}", userId);

        return client.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Get all users");

        return client.findAllUsers();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        log.info("Delete user, userId={}", userId);

        return client.deleteUserById(userId);
    }
}