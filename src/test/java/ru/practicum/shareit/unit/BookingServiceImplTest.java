package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    @BeforeEach
    public void setUp() {
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

    @Test
    public void getExceptionWhenBookerIsOwner() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        final ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
                () -> bookingService.saveBooking(1L, new BookingFromRequest()));

        assertEquals(BOOKER_IS_OWNER_MESSAGE + 1, exception.getMessage());
    }

    @Test
    public void getExceptionWhenItemNotAvailableForBooking() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingService.saveBooking(2L, new BookingFromRequest()));

        assertEquals(ITEM_NOT_AVAILABLE_MESSAGE, exception.getMessage());
    }

    @Test
    public void getExceptionWhenBookingAlreadyApprove() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        final NotAvailableException exception = assertThrows(NotAvailableException.class,
                () -> bookingService.approveBooking(1L, 1L, "true"));

        assertEquals(NOT_AVAILABLE_APPROVE_MESSAGE, exception.getMessage());
    }

    @Test
    public void bookingFilterTest() {
        final Booking pastBooking = booking.toBuilder()
                .start(LocalDateTime.now().minusMinutes(15))
                .end(LocalDateTime.now().minusMinutes(10))
                .status(BookingStatus.APPROVED)
                .build();
        final Booking futureBooking = booking.toBuilder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(20))
                .status(BookingStatus.APPROVED)
                .build();
        final Booking rejectedBooking = booking.toBuilder()
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(20))
                .status(BookingStatus.REJECTED)
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> bookingList = List.of(pastBooking, futureBooking, rejectedBooking);
        Page<Booking> bookingPage = new PageImpl<>(bookingList, pageRequest, bookingList.size());
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByUserIdOrderByStartDesc(any(), any())).thenReturn(bookingPage);

        List<Booking> past = bookingService.getAllUserBookings(1L,
                BookingFilterState.PAST.name(), 0, 10);
        List<Booking> future = bookingService.getAllUserBookings(1L,
                BookingFilterState.FUTURE.name(), 0, 10);
        List<Booking> rejected = bookingService.getAllUserBookings(1L,
                BookingFilterState.REJECTED.name(), 0, 10);

        assertEquals(1, past.size());
        assertEquals(pastBooking, past.get(0));
        assertEquals(1, future.size());
        assertEquals(futureBooking, future.get(0));
        assertEquals(1, rejected.size());
        assertEquals(rejectedBooking, rejected.get(0));
    }
}