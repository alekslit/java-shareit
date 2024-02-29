package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingForItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    // идентификатор предмета:
    private Long id;

    // название предмета:
    @NotBlank(message = "Название предмета (name) не может быть пустым")
    private String name;

    // описание предмета:
    @NotBlank(message = "Описание предмета (description) не может быть пустым")
    private String description;

    // доступно для аренды или нет:
    @NotNull(message = "Поле доступность для аренды (available), не может быть пустым.")
    private Boolean available;

    // информация об аренде:
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

    // комментарии от пользователей:
    private List<CommentDto> comments;
}