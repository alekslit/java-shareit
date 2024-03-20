package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    // идентификатор предмета:
    private Long id;

    // название предмета:
    private String name;

    // описание предмета:
    private String description;

    // доступно для аренды или нет:
    private Boolean available;

    // информация об аренде:
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

    // комментарии от пользователей:
    private List<CommentDto> comments;

    // id запроса:
    private Long requestId;
}