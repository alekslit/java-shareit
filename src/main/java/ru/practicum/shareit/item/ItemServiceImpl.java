package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        log.debug("Попытка добавить новый объект Item.");
        userRepository.getUserById(userId);
        return itemRepository.saveItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        log.debug("Попытка обновить информацию об объекте Item.");
        return itemRepository.updateItem(userId, itemDto, itemId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.debug("Попытка получить объект Item по его id.");
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(Long userId) {
        log.debug("Попытка получить список объектов Item по id пользователя (владельца).");
        return itemRepository.findAllItemsByOwnerId(userId);
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text) {
        log.debug("Попытка найти список объектов Item по их названию и/или описанию.");
        return itemRepository.searchItemsByNameOrDescription(text);
    }
}