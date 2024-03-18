package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto saveItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ItemRequestMapper.mapToItemRequestDto(service.saveItemRequest(userId, itemRequestDto));
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemRequest> itemRequestList = service.findAllByUserId(userId);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.mapToItemRequestDto(itemRequestList);
        itemRequestDtoList = service.addItemToItemRequest(itemRequestDtoList);

        return itemRequestDtoList;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        List<ItemRequest> itemRequestList = service.findAllFromOtherUsers(userId, from, size);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.mapToItemRequestDto(itemRequestList);
        itemRequestDtoList = service.addItemToItemRequest(itemRequestDtoList);

        return itemRequestDtoList;
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        ItemRequest itemRequest = service.getItemRequestById(userId, requestId);
        ItemRequestDto itemRequestDto = ItemRequestMapper.mapToItemRequestDto(itemRequest);
        itemRequestDto = service.addItemToItemRequest(itemRequestDto);

        return itemRequestDto;
    }
}