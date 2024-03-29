package ru.practicum.shareit.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=test",
                "spring.datasource.password=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final ItemController itemController;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private UserDto userDto;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private BookingFromRequest lastBooking;
    private BookingFromRequest nextBooking;
    private UserDto userDto2;
    private ItemDto itemDto2;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .name("test1")
                .email("test1")
                .build();
        itemDto = ItemDto.builder()
                .name("test2")
                .description("test2")
                .available(true)
                .build();
        commentDto = CommentDto.builder()
                .text("test3")
                .build();
        lastBooking = BookingFromRequest.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(50))
                .build();
        nextBooking = BookingFromRequest.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();
        userDto2 = UserDto.builder()
                .name("booker")
                .email("booker")
                .build();
        itemDto2 = ItemDto.builder()
                .name("test4")
                .description("test4")
                .available(true)
                .build();
    }

    @Test
    public void getItemById() {
        User user = userService.saveUser(userDto);
        User booker = userService.saveUser(userDto2);
        Item item = itemService.saveItem(user.getId(), itemDto);
        lastBooking.setItemId(item.getId());
        nextBooking.setItemId(item.getId());
        Booking last = bookingService.saveBooking(booker.getId(), lastBooking);
        bookingService.approveBooking(user.getId(), last.getId(), "true");
        Booking next = bookingService.saveBooking(booker.getId(), nextBooking);
        bookingService.approveBooking(user.getId(), next.getId(), "true");
        Comment comment = itemService.saveComment(booker.getId(), item.getId(), commentDto);

        ItemDto itemById = itemController.getItemById(user.getId(), item.getId());

        assertEquals(item.getId(), itemById.getId());
        assertEquals(item.getName(), itemById.getName());
        assertEquals(item.getDescription(), itemById.getDescription());
        assertEquals(comment.getText(), itemById.getComments().get(0).getText());
        assertEquals(last.getId(), itemById.getLastBooking().getId());
        assertEquals(next.getId(), itemById.getNextBooking().getId());
    }

    @Test
    public void findAllItemsByOwnerId() {
        User user = userService.saveUser(userDto);
        Item item = itemService.saveItem(user.getId(), itemDto);
        Item item2 = itemService.saveItem(user.getId(), itemDto2);

        List<ItemDto> itemList = itemController.findAllItemsByOwnerId(user.getId(), 0, 10);

        assertEquals(2, itemList.size());
        assertEquals(item.getId(), itemList.get(0).getId());
        assertEquals(item2.getId(), itemList.get(1).getId());
    }

    @Test
    public void searchItemsByNameOrDescriptionTest() {
        User user = userService.saveUser(userDto);
        Item item = itemService.saveItem(user.getId(), itemDto);
        Item item2 = itemService.saveItem(user.getId(), itemDto2);

        List<ItemDto> itemList = itemController.searchItemsByNameOrDescription("test2", 0, 10);

        assertEquals(1, itemList.size());
        assertEquals(item.getId(), itemList.get(0).getId());
        assertEquals("test2", item.getName());
    }
}