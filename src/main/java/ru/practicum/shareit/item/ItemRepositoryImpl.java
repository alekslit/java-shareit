package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ForbiddenOperationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_ADVICE;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_MESSAGE;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currentIdNumber = 0L;

    /*---------Основные методы----------*/
    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        Item item = itemDtoToItem(itemDto);
        item = item.toBuilder()
                .id(generatedId())
                .ownerId(userId)
                .build();
        items.put(item.getId(), item);

        return itemToDtoItem(item);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        Item item = items.get(itemId);
        checkOwnerItem(userId, item);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(item.getId(), item);

        return itemToDtoItem(item);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = items.get(itemId);

        return itemToDtoItem(item);
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(Long userId) {
        List<ItemDto> itemDtoList = items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(this::itemToDtoItem)
                .collect(Collectors.toList());

        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        List<ItemDto> itemDtoList = items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(this::itemToDtoItem)
                .collect(Collectors.toList());

        return itemDtoList;
    }

    /*---------Вспомогательные методы---------*/
    private Long generatedId() {
        return ++currentIdNumber;
    }

    // метод для преобразования Item в ItemDto:
    private ItemDto itemToDtoItem(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        return itemDto;
    }

    // метод для преобразования ItemDto в Item:
    private Item itemDtoToItem(ItemDto itemDto) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();

        return item;
    }

    // метод для проверки владельца предмета:
    private void checkOwnerItem(Long userId, Item item) {
        if (!item.getOwnerId().equals(userId)) {
            log.debug("{}: {}{}.", ForbiddenOperationException.class.getSimpleName(), NOT_OWNER_ITEM_MESSAGE, userId);
            throw new ForbiddenOperationException(NOT_OWNER_ITEM_MESSAGE + userId, NOT_OWNER_ITEM_ADVICE);
        }
    }
}