package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private ItemController controller;
    @Mock
    private ItemClient client;

    @BeforeEach
    public void setUp() {
        controller = new ItemController(client);
    }
/*    @Test
    public void tets() {
        ResponseEntity<Object> response = controller.saveItem(1L, ItemDto.builder().build());
        System.out.println(response);
    }*/
}