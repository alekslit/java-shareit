package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingFromRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserForBookingDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {
    // метод для преобразования в Booking:
    public static Booking mapToBooking(BookingFromRequest bookingFromRequest, User user, Item item) {
        Booking booking = Booking.builder()
                .status(BookingStatus.WAITING)
                .start(bookingFromRequest.getStart())
                .end(bookingFromRequest.getEnd())
                .user(user)
                .item(item)
                .build();

        return booking;
    }

    // метод для преобразования в BookingDto:
    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserForBookingDto.builder()
                        .id(booking.getUser().getId())
                        .build())
                .item(ItemForBookingDto.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .build();

        return bookingDto;
    }

    // метод для преобразования в список BookingDto:
    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        List<BookingDto> boookingDtoList = bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());

        return boookingDtoList;
    }

    public static BookingForItemDto mapToBookingForItemDto(Booking booking) {
        BookingForItemDto bookingForItemDto = BookingForItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();

        return bookingForItemDto;
    }
}