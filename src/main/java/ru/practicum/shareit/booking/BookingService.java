package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    Booking saveBooking(Long userId, BookingFromRequest bookingFromRequest);

    Booking approveBooking(Long userId, Long bookingId, String approved);

    Booking getBookingById(Long bookingId);

    List<Booking> getAllUserBookings(Long userId, String state);

    List<Booking> getAllOwnerItemsBookings(Long userId, String state);
}