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
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return service.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {

        return service.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return service.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> findAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.findAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        return service.searchItemsByNameOrDescription(text);
    }
}