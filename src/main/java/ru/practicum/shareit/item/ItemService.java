package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> findAllItemsByOwnerId(Long userId);

    List<ItemDto> searchItemsByNameOrDescription(String text);
}