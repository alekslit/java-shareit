package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.NotAvailableException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_DATE_TIME_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private BookingController bookingController;
    @Mock
    private BookingClient client;
    private BookingFromRequest booking;

    @BeforeEach
    public void setUp() {
        bookingController = new BookingController(client);
        booking = BookingFromRequest.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    public void getExceptionWhenEndBeforeStart() {
        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingController.saveBooking(1L, booking));

        assertEquals(NOT_AVAILABLE_DATE_TIME_MESSAGE, exception.getMessage());
    }
}