package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_ADVICE;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_DATE_TIME_ADVICE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_DATE_TIME_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService service;

    /*---------Основные методы---------*/
    @PostMapping
    public BookingDto saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody BookingFromRequest bookingFromRequest) {
        checkBookingDateTime(bookingFromRequest);

        return BookingMapper.mapToBookingDto(service.saveBooking(userId, bookingFromRequest));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam String approved) {
        return BookingMapper.mapToBookingDto(service.approveBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        Booking booking = service.getBookingById(bookingId);
        checkBookingAuthorAndItemOwner(booking, userId);

        return BookingMapper.mapToBookingDto(booking);
    }

    // получение списка бронирований для пользователя (автора бронирования):
    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapToBookingDto(service.getAllUserBookings(userId, state));
    }

    // получение списка бронирований всех предметов пользователя (владельца предметов):
    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapToBookingDto(service.getAllOwnerItemsBookings(userId, state));
    }

    /*---------Вспомогательные методы---------*/
    // проверяем, чтобы информацию запрашивал либо владелец предмета, либо автор бронирования:
    private void checkBookingAuthorAndItemOwner(Booking booking, Long userId) {
        if (!booking.getUser().getId().equals(userId) && !booking.getItem().getUser().getId().equals(userId)) {
            log.debug("{}: {}{}.", ForbiddenOperationException.class.getSimpleName(),
                    NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE, userId);
            throw new ForbiddenOperationException(NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE + userId,
                    NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_ADVICE);
        }
    }

    // проверяем даты аренды в объекте из запроса:
    private void checkBookingDateTime(BookingFromRequest bookingFromRequest) {
        if ((bookingFromRequest.getStart() == null || bookingFromRequest.getEnd() == null)
                || bookingFromRequest.getStart().equals(bookingFromRequest.getEnd())
                || (bookingFromRequest.getEnd().isBefore(LocalDateTime.now())
                || bookingFromRequest.getStart().isBefore(LocalDateTime.now()))
                || bookingFromRequest.getEnd().isBefore(bookingFromRequest.getStart())) {
            log.debug("{}: {}.", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_DATE_TIME_MESSAGE);
            throw new NotAvailableException(NOT_AVAILABLE_DATE_TIME_MESSAGE, NOT_AVAILABLE_DATE_TIME_ADVICE);
        }
    }
}