package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .user(user)
                .creationDate(LocalDateTime.now())
                .build();

        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreationDate())
                .build();

        return itemRequestDto;
    }

    public static List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> itemRequestList) {
        List<ItemRequestDto> itemRequestDtoList = itemRequestList.stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());

        return itemRequestDtoList;
    }
}