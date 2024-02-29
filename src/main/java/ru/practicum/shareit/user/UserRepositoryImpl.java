package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.AlreadyExistException.DUPLICATE_EMAIL_ADVICE;
import static ru.practicum.shareit.exception.AlreadyExistException.DUPLICATE_EMAIL_MESSAGE;
import static ru.practicum.shareit.exception.NotFoundException.USER_NOT_FOUND_ADVICE;
import static ru.practicum.shareit.exception.NotFoundException.USER_NOT_FOUND_MESSAGE;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long currentIdNumber = 0L;

    /*---------Основные методы---------*/
    @Override
    public User saveUser(User user) {
        checkEmailForDuplicate(user.getEmail());
        user.setId(generatedId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user, Long userId) {
        User updateUser = users.get(userId);

        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!updateUser.getEmail().equals(user.getEmail())) {
                checkEmailForDuplicate(user.getEmail());
                updateUser.setEmail(user.getEmail());
            }
        }
        users.put(userId, updateUser);

        return updateUser;
    }

    @Override
    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), USER_NOT_FOUND_MESSAGE, userId);
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE + userId, USER_NOT_FOUND_ADVICE);
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    /*---------Вспомогательные методы---------*/
    private Long generatedId() {
        return ++currentIdNumber;
    }

    @Override
    public void checkEmailForDuplicate(String email) {
        List<String> emailList = users.values().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        if (emailList.contains(email)) {
            log.debug("{}: {}{}.", AlreadyExistException.class.getSimpleName(), DUPLICATE_EMAIL_MESSAGE, email);
            throw new AlreadyExistException(DUPLICATE_EMAIL_MESSAGE + email, DUPLICATE_EMAIL_ADVICE);
        }
    }
}