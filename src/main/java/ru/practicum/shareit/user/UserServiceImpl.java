package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User saveUser(User user) {
        log.debug("Попытка добавить новый объект User.");
        return repository.saveUser(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        log.debug("Попытка обновить информацию об объекте User.");
        return repository.updateUser(user, userId);
    }

    @Override
    public User getUserById(Long userId) {
        log.debug("Попытка получить объект User по его id.");
        return repository.getUserById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Попытка получить список всех объектов User.");
        return repository.findAllUsers();
    }

    @Override
    public void deleteUserById(Long userId) {
        log.debug("Попытка удалить объект User.");
        repository.deleteUserById(userId);
    }
}