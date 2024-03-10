package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingFromRequest;

import java.util.List;

public interface BookingService {
    Booking saveBooking(Long userId, BookingFromRequest bookingFromRequest);

    Booking approveBooking(Long userId, Long bookingId, String approved);

    Booking getBookingById(Long bookingId);

    List<Booking> getAllUserBookings(Long userId, String state, int from, int size);

    List<Booking> getAllOwnerItemsBookings(Long userId, String state, int from, int size);
}