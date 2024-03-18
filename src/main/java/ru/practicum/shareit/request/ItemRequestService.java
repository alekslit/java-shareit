package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequest saveItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequest> findAllByUserId(Long userId);

    List<ItemRequestDto> addItemToItemRequest(List<ItemRequestDto> itemRequestDtoList);

    List<ItemRequest> findAllFromOtherUsers(Long userId, int from, int size);

    ItemRequest getItemRequestById(Long userId, Long requestId);

    ItemRequestDto addItemToItemRequest(ItemRequestDto itemRequestDto);
}