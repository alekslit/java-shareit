package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User saveUser(User user) {
        return repository.saveUser(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        return repository.updateUser(user, userId);
    }

    @Override
    public User getUserById(Long userId) {
        return repository.getUserById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }

    @Override
    public void deleteUserById(Long userId) {
        repository.deleteUserById(userId);
    }
}