package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);

    User updateUser(User user, Long userId);

    User getUserById(Long userId);

    List<User> findAllUsers();

    void deleteUserById(Long userId);

    void checkEmailForDuplicate(String email);
}