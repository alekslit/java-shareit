package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTestWithDb {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private UserDto user1;
    private ItemDto item;
    private UserDto user2;
    private BookingFromRequest booking1;
    private BookingFromRequest booking2;

    public void init() {
        user1 = UserDto.builder()
                .name("test1")
                .email("test1")
                .build();
        item = ItemDto.builder()
                .name("test2")
                .description("test2")
                .available(true)
                .build();
        user2 = UserDto.builder()
                .name("booker")
                .email("booker")
                .build();
        booking1 = BookingFromRequest.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        booking2 = BookingFromRequest.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void getAllUserBookingsTest() {
        User owner = userService.saveUser(user1);
        User booker = userService.saveUser(user2);
        Item userItem = itemService.saveItem(owner.getId(), item);
        booking1.setItemId(userItem.getId());
        booking2.setItemId(userItem.getId());
        Booking savedBooking1 = bookingService.saveBooking(booker.getId(), booking1);
        Booking savedBooking2 = bookingService.saveBooking(booker.getId(), booking2);

        List<Booking> bookingList = bookingService.getAllUserBookings(booker.getId(),
                "WAITING", 0, 10);

        assertEquals(2, bookingList.size());
        assertEquals(savedBooking2, bookingList.get(0));
        assertEquals(savedBooking1, bookingList.get(1));
    }

    @Test
    public void getAllOwnerItemsBookingsTest() {
        User owner = userService.saveUser(user1);
        User booker = userService.saveUser(user2);
        Item userItem = itemService.saveItem(owner.getId(), item);
        booking1.setItemId(userItem.getId());
        booking2.setItemId(userItem.getId());
        Booking savedBooking1 = bookingService.saveBooking(booker.getId(), booking1);
        Booking savedBooking2 = bookingService.saveBooking(booker.getId(), booking2);

        List<Booking> bookingList = bookingService.getAllOwnerItemsBookings(owner.getId(),
                "CURRENT", 0, 10);

        assertEquals(1, bookingList.size());
        assertEquals(savedBooking1, bookingList.get(0));
    }
}