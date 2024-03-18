package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User saveUser(UserDto userDto);

    User updateUser(UserDto userDto, Long userId);

    User getUserById(Long userId);

    List<User> findAllUsers();

    void deleteUserById(Long userId);
}