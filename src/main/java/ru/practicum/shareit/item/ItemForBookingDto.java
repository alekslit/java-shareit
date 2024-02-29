package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemForBookingDto {
    private Long id;
    private String name;
}