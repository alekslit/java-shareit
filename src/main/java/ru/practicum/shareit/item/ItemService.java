package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item saveItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, ItemDto itemDto, Long itemId);

    Item getItemById(Long itemId);

    List<Item> findAllItemsByOwnerId(Long userId);

    List<Item> searchItemsByNameOrDescription(String text);

    ItemDto addBookingsDtoToItem(ItemDto itemDto);

    List<ItemDto> addBookingsDtoToItem(List<ItemDto> itemDtoList);

    Comment saveComment(Long userId, Long itemId, CommentDto commentDto);

    ItemDto addCommentToItem(ItemDto itemDto);

    List<ItemDto> addCommentToItem(List<ItemDto> itemDtoList);
}