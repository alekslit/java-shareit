package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ItemMapper {
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

    // метод для преобразования ItemDto в Item с id:
    public static Item mapToItem(ItemDto itemDto, User user, Long itemId) {
        Item item = Item.builder()
                .id(itemId)
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
}