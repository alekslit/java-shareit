package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.user.UserForBookingDto;

import java.time.LocalDateTime;

// сущность для ответа на запрос:
@Data
@Builder(toBuilder = true)
public class BookingDto {
    private Long id;
    private BookingStatus status;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserForBookingDto booker;
    private ItemForBookingDto item;
}