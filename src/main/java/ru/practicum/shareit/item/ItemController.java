package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.mapToItemDto(service.saveItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {

        return ItemMapper.mapToItemDto(service.updateItem(userId, itemDto, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long itemId) {
        Item item = service.getItemById(itemId);
        // если запрос делал владелец, то добавим информацию о бронировании:
        if (item.getUser().getId().equals(userId)) {
            ItemDto itemWithBookings = service.addBookingsDtoToItem(ItemMapper.mapToItemDto(item));

            return service.addCommentToItem(itemWithBookings);
        }
        ItemDto itemDto = ItemMapper.mapToItemDto(item);

        return service.addCommentToItem(itemDto);
    }

    @GetMapping
    public List<ItemDto> findAllItemsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        List<Item> itemList = service.findAllItemsByOwnerId(userId, from, size);
        List<ItemDto> itemWithBookings = service.addBookingsDtoToItem(ItemMapper.mapToItemDto(itemList));

        return service.addCommentToItem(itemWithBookings);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        return ItemMapper.mapToItemDto(service.searchItemsByNameOrDescription(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId,
                                  @Valid @RequestBody CommentDto commentDto) {
        return CommentMapper.mapToCommentDto(service.saveComment(userId, itemId, commentDto));
    }
}