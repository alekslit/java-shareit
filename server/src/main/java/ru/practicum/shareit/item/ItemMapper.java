package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    // метод для преобразования ItemDto в Item:
    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .user(user)
                .build();

        return item;
    }

    // метод для преобразования Item в ItemDto:
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest() != null ? item.getItemRequest().getId() : null)
                .build();

        return itemDto;
    }

    // метод для преобразования списка Item в список ItemDto:
    public static List<ItemDto> mapToItemDto(List<Item> items) {
        List<ItemDto> itemDtoList = items.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());

        return itemDtoList;
    }

    public static ItemForItemRequest mapToItemForItemRequest(Item item) {
        ItemForItemRequest itemForItemRequest = ItemForItemRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest().getId())
                .build();

        return itemForItemRequest;
    }

    public static List<ItemForItemRequest> mapToItemForItemRequest(List<Item> itemList) {
        List<ItemForItemRequest> itemForItemRequestList = itemList.stream()
                .map(ItemMapper::mapToItemForItemRequest)
                .collect(Collectors.toList());

        return itemForItemRequestList;
    }
}