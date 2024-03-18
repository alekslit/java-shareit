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
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_COMMENT_MESSAGE;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=test",
                "spring.datasource.password=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    private UserDto userDto;
    private ItemDto itemDto;
    private UserDto userDto2;
    private BookingFromRequest lastBooking;
    private BookingFromRequest nextBooking;
    private CommentDto commentDto;

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
        userDto2 = UserDto.builder()
                .name("booker")
                .email("booker")
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
    }

    @Test
    public void saveItem() {
        User user = userService.saveUser(userDto);
        Item item = itemService.saveItem(user.getId(), itemDto);

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(userDto.getName(), item.getUser().getName());
        assertEquals(userDto.getEmail(), item.getUser().getEmail());
    }

    @Test
    public void updateItem() {
        User user = userService.saveUser(userDto);
        Item item = itemService.saveItem(user.getId(), itemDto);
        ItemDto itemUpdate = ItemDto.builder()
                .name("update")
                .description("update")
                .available(false)
                .build();

        Item result = itemService.updateItem(user.getId(), itemUpdate, item.getId());

        assertEquals(itemUpdate.getName(), result.getName());
        assertEquals(itemUpdate.getDescription(), result.getDescription());
        assertEquals(itemUpdate.getAvailable(), result.getAvailable());
    }

    @Test
    public void getExceptionWhenSaveCommentWithNotApproveBooking() {
        User user = userService.saveUser(userDto);
        User booker = userService.saveUser(userDto2);
        Item item = itemService.saveItem(user.getId(), itemDto);
        lastBooking.setItemId(item.getId());
        nextBooking.setItemId(item.getId());
        Booking last = bookingService.saveBooking(booker.getId(), lastBooking);
        Booking next = bookingService.saveBooking(booker.getId(), nextBooking);

        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> itemService.saveComment(booker.getId(), item.getId(), commentDto));

        assertEquals(NOT_AVAILABLE_COMMENT_MESSAGE, exception.getMessage());
    }
}