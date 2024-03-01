package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

// сущность для приема запроса:
@Data
public class BookingFromRequest {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}