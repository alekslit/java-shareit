package ru.practicum.shareit.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Item item;
    private User user;

    public void init() {
        user = User.builder()
                .name("test2")
                .email("test2")
                .build();
        item = Item.builder()
                .name("test1")
                .description("search")
                .available(true)
                .user(user)
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
    }

    // тест для метода репозитория написанного через @Query:
    @Test
    public void searchItemsByNameOrDescription() {
        userRepository.save(user);
        Item savedItem = itemRepository.save(item);
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Item> emptyList = itemRepository.searchItemsByNameOrDescription("empty", pageRequest).getContent();
        List<Item> listWithItem = itemRepository.searchItemsByNameOrDescription("sea", pageRequest).getContent();

        assertEquals(0, emptyList.size());
        assertEquals(1, listWithItem.size());
        assertEquals(savedItem, listWithItem.get(0));
    }
}