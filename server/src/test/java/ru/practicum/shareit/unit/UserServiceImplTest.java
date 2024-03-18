package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.AlreadyExistException.DUPLICATE_EMAIL_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private UserService userService;
    private UserDto userDto;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userDto = UserDto.builder()
                .name("test1")
                .email("test1")
                .build();
    }

    @Test
    public void exceptionWhenSaveUser() {
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException(""));

        final AlreadyExistException exception = assertThrows(AlreadyExistException.class,
                () -> userService.saveUser(userDto));

        assertEquals(DUPLICATE_EMAIL_MESSAGE + userDto.getEmail(),
                exception.getMessage());
    }

    @Test
    public void exceptionWhenUpdateUser() {
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException(""));
        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(UserMapper.mapToUser(userDto)));

        final AlreadyExistException exception = assertThrows(AlreadyExistException.class,
                () -> userService.updateUser(userDto, userDto.getId()));

        assertEquals(DUPLICATE_EMAIL_MESSAGE + userDto.getEmail(),
                exception.getMessage());
    }
}