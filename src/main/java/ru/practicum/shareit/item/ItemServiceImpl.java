package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        userRepository.getUserById(userId);

        return itemRepository.saveItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        return itemRepository.updateItem(userId, itemDto, itemId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(Long userId) {
        return itemRepository.findAllItemsByOwnerId(userId);
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text) {
        return itemRepository.searchItemsByNameOrDescription(text);
    }
}