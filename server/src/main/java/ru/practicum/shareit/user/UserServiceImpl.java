package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

import static ru.practicum.shareit.exception.AlreadyExistException.DUPLICATE_EMAIL_ADVICE;
import static ru.practicum.shareit.exception.AlreadyExistException.DUPLICATE_EMAIL_MESSAGE;
import static ru.practicum.shareit.exception.NotFoundException.USER_NOT_FOUND_ADVICE;
import static ru.practicum.shareit.exception.NotFoundException.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    /*------Основные методы------*/
    @Override
    public User saveUser(UserDto userDto) {
        log.debug("Попытка добавить новый объект User.");
        User user = UserMapper.mapToUser(userDto);
        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.debug("{}: {}{}.", AlreadyExistException.class.getSimpleName(),
                    DUPLICATE_EMAIL_MESSAGE, userDto.getEmail());
            throw new AlreadyExistException(DUPLICATE_EMAIL_MESSAGE + userDto.getEmail(), DUPLICATE_EMAIL_ADVICE);
        }

        return user;
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        log.debug("Попытка обновить информацию об объекте User.");
        User user = updateUserObject(userDto, userId);
        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.debug("{}: {}{}.", AlreadyExistException.class.getSimpleName(),
                    DUPLICATE_EMAIL_MESSAGE, userDto.getEmail());
            throw new AlreadyExistException(DUPLICATE_EMAIL_MESSAGE + userDto.getEmail(), DUPLICATE_EMAIL_ADVICE);
        }

        return user;
    }

    @Override
    public User getUserById(Long userId) {
        log.debug("Попытка получить объект User по его id.");
        User user = repository.findById(userId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), USER_NOT_FOUND_MESSAGE, userId);
            return new NotFoundException(USER_NOT_FOUND_MESSAGE + userId, USER_NOT_FOUND_ADVICE);
        });

        return user;
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Попытка получить список всех объектов User.");
        List<User> userList = repository.findAll();

        return userList;
    }

    @Override
    public void deleteUserById(Long userId) {
        log.debug("Попытка удалить объект User.");
        repository.deleteById(userId);
    }

    /*------Вспомогательные методы------*/
    private User updateUserObject(UserDto userDto, Long userId) {
        // получаем объект из БД:
        User user = getUserById(userId);
        // обновляем данные, если они инициализированы:
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());

        return user;
    }
}