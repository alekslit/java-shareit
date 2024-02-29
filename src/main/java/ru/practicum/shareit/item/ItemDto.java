package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
}