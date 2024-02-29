package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public User saveUser(@Valid @RequestBody User user) {
        return service.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable Long userId) {
        return service.updateUser(user, userId);
    }

    @GetMapping("{userId}")
    User getUserById(@PathVariable Long userId) {
        return service.getUserById(userId);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return service.findAllUsers();
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        service.deleteUserById(userId);
    }
}