package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_DATE_TIME_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    private Booking booking1;
    private BookingFromRequest booking2;

    @BeforeEach
    public void setUp() {
        bookingController = new BookingController(bookingService);
        booking1 = Booking.builder()
                .item(Item.builder()
                        .user(User.builder().id(1L).build())
                        .build())
                .user(User.builder().id(2L).build())
                .build();
        booking2 = BookingFromRequest.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    public void getExceptionWhenNotOwnerOrAuthor() {
        Mockito.when(bookingService.getBookingById(Mockito.any())).thenReturn(booking1);

        final ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
                () -> bookingController.getBookingById(3L, 1L));

        assertEquals(NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE + 3, exception.getMessage());
    }

    @Test
    public void getExceptionWhenEndBeforeStart() {
        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingController.saveBooking(1L, booking2));

        assertEquals(NOT_AVAILABLE_DATE_TIME_MESSAGE, exception.getMessage());
    }
}