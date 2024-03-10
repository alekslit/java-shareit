package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.exception.ForbiddenOperationException.BOOKER_IS_OWNER_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.ITEM_NOT_AVAILABLE_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_APPROVE_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    private BookingService bookingService;
    private Item item;
    private Booking booking;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    public void init() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        item = Item.builder()
                .available(false)
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        booking = Booking.builder()
                .status(BookingStatus.APPROVED)
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void getExceptionWhenBookerIsOwner() {
        Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));

        final ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
                () -> bookingService.saveBooking(1L, new BookingFromRequest()));

        assertEquals(BOOKER_IS_OWNER_MESSAGE + 1, exception.getMessage());
    }

    @Test
    public void getExceptionWhenItemNotAvailableForBooking() {
        Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));

        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingService.saveBooking(2L, new BookingFromRequest()));

        assertEquals(ITEM_NOT_AVAILABLE_MESSAGE, exception.getMessage());
    }

    @Test
    public void getExceptionWhenBookingAlreadyApprove() {
        Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(Optional.of(booking));

        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingService.approveBooking(1L, 1L, "true"));

        assertEquals(NOT_AVAILABLE_APPROVE_MESSAGE, exception.getMessage());
    }
}