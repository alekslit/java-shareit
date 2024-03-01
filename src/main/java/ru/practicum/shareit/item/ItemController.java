package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
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
    public List<ItemDto> findAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> itemList = service.findAllItemsByOwnerId(userId);
        List<ItemDto> itemWithBookings = service.addBookingsDtoToItem(ItemMapper.mapToItemDto(itemList));

        return service.addCommentToItem(itemWithBookings);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        return ItemMapper.mapToItemDto(service.searchItemsByNameOrDescription(text));
    }

    @PostMapping("{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId,
                                  @Valid @RequestBody CommentDto commentDto) {
        return CommentMapper.mapToCommentDto(service.saveComment(userId, itemId, commentDto));
    }
}