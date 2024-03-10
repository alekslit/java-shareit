package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.NotFoundException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /*---------Основные методы---------*/
    @Override
    public ItemRequest saveItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("Попытка добавить новый объект ItemRequest.");
        User user = getUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(itemRequestDto, user));

        return itemRequest;
    }

    @Override
    public List<ItemRequest> findAllByUserId(Long userId) {
        log.debug("Попытка получить список ItemRequest по id пользователя.");
        // проверяем существует ли пользователь:
        getUserById(userId);
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByUserIdOrderByCreationDateDesc(userId);

        return itemRequestList;
    }

    @Override
    public List<ItemRequestDto> addItemToItemRequest(List<ItemRequestDto> itemRequestDtoList) {
        // итоговый список:
        List<ItemRequestDto> result = new ArrayList<>();
        // получаем id всех ItemRequest:
        List<Long> requestIdList = getAllItemRequestId(itemRequestDtoList);
        // находим все Item:
        List<Item> itemList = itemRepository.findAllByItemRequestIdIn(requestIdList);
        // проходим по списку Item id-шниками ItemRequest:
        for (ItemRequestDto itemRequest : itemRequestDtoList) {
            List<Item> itemsForItemRequest = itemList.stream()
                    .filter(item -> item.getItemRequest().getId().equals(itemRequest.getId()))
                    .collect(Collectors.toList());
            // добавляем список Item к ItemRequest:
            itemRequest.setItems(ItemMapper.mapToItemForItemRequest(itemsForItemRequest));
            // сохраняем в список:
            result.add(itemRequest);
        }

        return result;
    }

    @Override
    public List<ItemRequest> findAllFromOtherUsers(Long userId, int from, int size) {
        log.debug("Попытка получить список ItemRequest других пользователей.");
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemRequest> itemRequestList = itemRequestRepository
                .findAllByUserIdNotOrderByCreationDateDesc(userId, pageRequest)
                .getContent();

        return itemRequestList;
    }

    @Override
    public ItemRequest getItemRequestById(Long userId, Long requestId) {
        log.debug("Попытка получить объект ItemRequest по его id.");
        // проверяем существует ли пользователь:
        getUserById(userId);
        ItemRequest itemRequest = getItemRequestById(requestId);

        return itemRequest;
    }

    @Override
    public ItemRequestDto addItemToItemRequest(ItemRequestDto itemRequestDto) {
        List<Item> itemList = itemRepository.findAllByItemRequestId(itemRequestDto.getId());
        itemRequestDto.setItems(ItemMapper.mapToItemForItemRequest(itemList));

        return itemRequestDto;
    }

    /*---------Вспомогательные методы---------*/
    private User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), USER_NOT_FOUND_MESSAGE, userId);
            return new NotFoundException(USER_NOT_FOUND_MESSAGE + userId, USER_NOT_FOUND_ADVICE);
        });

        return user;
    }

    private List<Long> getAllItemRequestId(List<ItemRequestDto> itemRequestDtoList) {
        return itemRequestDtoList.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList());
    }

    private ItemRequest getItemRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            log.debug("{}: {}{}.", NotFoundException.class.getSimpleName(), REQUEST_NOT_FOUND_MESSAGE, requestId);
            return new NotFoundException(REQUEST_NOT_FOUND_MESSAGE + requestId, REQUEST_NOT_FOUND_ADVICE);
        });

        return itemRequest;
    }
}