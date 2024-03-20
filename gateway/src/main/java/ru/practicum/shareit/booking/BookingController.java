package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.exception.NotAvailableException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.shareit.exception.NotAvailableException.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingClient client;

    /*---------Основные методы---------*/
    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody BookingFromRequest requestDto) {
        checkBookingDateTime(requestDto);
        log.info("Creating booking {}, userId={}", requestDto, userId);

        return client.saveBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam String approved) {
        log.info("Approve booking {}, userId={}", bookingId, userId);

        return client.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);

        return client.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        checkStateFilter(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        BookingFilterState stateFilter = BookingFilterState.valueOf(state);

        return client.getAllUserBookings(userId, stateFilter, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerItemsBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        checkStateFilter(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        BookingFilterState stateFilter = BookingFilterState.valueOf(state);

        return client.getAllOwnerItemsBookings(userId, stateFilter, from, size);
    }

    /*---------Вспомогательные методы---------*/
    // проверяем даты аренды в объекте из запроса:
    private void checkBookingDateTime(BookingFromRequest bookingFromRequest) {
        if ((bookingFromRequest.getStart() == null || bookingFromRequest.getEnd() == null)
                || bookingFromRequest.getStart().equals(bookingFromRequest.getEnd())
                || (bookingFromRequest.getEnd().isBefore(LocalDateTime.now())
                || bookingFromRequest.getStart().isBefore(LocalDateTime.now()))
                || bookingFromRequest.getEnd().isBefore(bookingFromRequest.getStart())) {
            log.debug("{}: {}", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_DATE_TIME_MESSAGE);
            throw new NotAvailableException(NOT_AVAILABLE_DATE_TIME_MESSAGE, NOT_AVAILABLE_DATE_TIME_ADVICE);
        }
    }

    private void checkStateFilter(String state) {
        boolean isCorrectState = Arrays.stream(BookingFilterState.values())
                .anyMatch(filterState -> filterState.name().equals(state));

        if (!isCorrectState) {
            log.debug("{}: {}{}.", NotAvailableException.class.getSimpleName(), NOT_AVAILABLE_STATE_MESSAGE, state);
            throw new NotAvailableException(NOT_AVAILABLE_STATE_MESSAGE + state, NOT_AVAILABLE_STATE_ADVICE);
        }
    }
}