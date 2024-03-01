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
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        return UserMapper.mapToUserDto(service.saveUser(userDto));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        return UserMapper.mapToUserDto(service.updateUser(userDto, userId));
    }

    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return UserMapper.mapToUserDto(service.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return UserMapper.mapToUserDto(service.findAllUsers());
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        service.deleteUserById(userId);
    }
}