package ru.practicum.shareit.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.NotFoundException.USER_NOT_FOUND_MESSAGE;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserService userService;
    private UserDto user1;
    private UserDto user2;

    public void init() {
        user1 = UserDto.builder()
                .name("test1")
                .email("test1")
                .build();
        user2 = UserDto.builder()
                .name("test2")
                .email("test2")
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void updateUserTest() {
        User user = userService.saveUser(user1);
        UserDto userForUpdate = user1.toBuilder()
                .id(user.getId())
                .name("update")
                .email("update")
                .build();

        User result = userService.updateUser(userForUpdate, userForUpdate.getId());

        assertEquals(result.getId(), userForUpdate.getId());
        assertEquals(result.getName(), userForUpdate.getName());
        assertEquals(result.getEmail(), userForUpdate.getEmail());
    }

    @Test
    public void getExceptionAfterDeleteUserById() {
        User user = userService.saveUser(user1);
        userService.deleteUserById(user.getId());

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(user.getId()));

        assertEquals(USER_NOT_FOUND_MESSAGE + user.getId(), exception.getMessage());
    }

    @Test
    public void findAllUsersTest() {
        userService.saveUser(user1);
        userService.saveUser(user2);

        List<User> userList = userService.findAllUsers();

        assertEquals(2, userList.size());
    }
}