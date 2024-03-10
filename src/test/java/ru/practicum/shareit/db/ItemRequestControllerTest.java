package ru.practicum.shareit.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final ItemRequestController itemRequestController;
    private final ItemController itemController;
    private final UserService userService;
    private ItemRequestDto request1;
    private ItemRequestDto request2;
    private ItemRequestDto request3;
    private UserDto user1;
    private UserDto user2;
    private ItemDto item;
    private UserDto user3;

    public void init() {
        request1 = ItemRequestDto.builder()
                .description("test1")
                .build();
        request2 = ItemRequestDto.builder()
                .description("test2")
                .build();
        user1 = UserDto.builder()
                .name("test3")
                .email("test3")
                .build();
        request3 = ItemRequestDto.builder()
                .description("test4")
                .build();
        user2 = UserDto.builder()
                .name("test5")
                .email("test5")
                .build();
        item = ItemDto.builder()
                .name("test6")
                .description("test6")
                .available(true)
                .build();
        user3 = UserDto.builder()
                .name("test7")
                .email("test7")
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void getItemRequestByIdTest() {
        User savedUser1 = userService.saveUser(user1);
        ItemRequestDto savedRequest1 = itemRequestController.saveItemRequest(savedUser1.getId(), request1);
        User savedUser2 = userService.saveUser(user2);
        item.setRequestId(savedRequest1.getId());
        ItemDto savedItem = itemController.saveItem(savedUser2.getId(), item);

        ItemRequestDto request = itemRequestController.getItemRequestById(savedUser1.getId(),
                savedRequest1.getId());

        assertEquals(request.getId(), savedRequest1.getId());
        assertEquals(request.getItems().get(0).getId(), savedItem.getId());
        assertEquals(request.getDescription(), savedRequest1.getDescription());
        assertEquals(request.getItems().get(0).getName(), savedItem.getName());
    }

    @Test
    public void getAllItemRequestByUserIdTest() {
        // получим только запросы конкретного пользователя:
        User savedUser1 = userService.saveUser(user1);
        ItemRequestDto savedRequest1 = itemRequestController.saveItemRequest(savedUser1.getId(), request1);
        ItemRequestDto savedRequest2 = itemRequestController.saveItemRequest(savedUser1.getId(), request2);
        User savedUser2 = userService.saveUser(user2);
        ItemRequestDto savedRequest3 = itemRequestController.saveItemRequest(savedUser2.getId(), request3);
        item.setRequestId(savedRequest1.getId());
        ItemDto savedItem = itemController.saveItem(savedUser2.getId(), item);

        List<ItemRequestDto> requestList = itemRequestController.getAllItemRequestByUserId(savedUser1.getId());

        assertEquals(2, requestList.size());
        assertEquals(savedRequest2.getId(), requestList.get(0).getId());
        assertEquals(savedRequest1.getId(), requestList.get(1).getId());
        assertEquals(savedItem.getId(), requestList.get(1).getItems().get(0).getId());
    }

    @Test
    public void getAllItemRequestOtherUsersTest() {
        // получим запросы всех пользователей:
        User savedUser1 = userService.saveUser(user1);
        ItemRequestDto savedRequest1 = itemRequestController.saveItemRequest(savedUser1.getId(), request1);
        ItemRequestDto savedRequest2 = itemRequestController.saveItemRequest(savedUser1.getId(), request2);
        User savedUser2 = userService.saveUser(user2);
        ItemRequestDto savedRequest3 = itemRequestController.saveItemRequest(savedUser2.getId(), request3);
        item.setRequestId(savedRequest1.getId());
        ItemDto savedItem = itemController.saveItem(savedUser2.getId(), item);
        User savedUser3 = userService.saveUser(user3);

        List<ItemRequestDto> requestList = itemRequestController
                .getAllItemRequestOtherUsers(savedUser3.getId(), 0, 10);

        assertEquals(3, requestList.size());
        assertEquals(savedRequest3.getId(), requestList.get(0).getId());
        assertEquals(savedRequest2.getId(), requestList.get(1).getId());
        assertEquals(savedRequest1.getId(), requestList.get(2).getId());
        assertEquals(savedItem.getId(), requestList.get(2).getItems().get(0).getId());
    }
}