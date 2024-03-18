package ru.practicum.shareit.web;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final ItemController itemController;

    @Test
    public void getExceptionWhenFromIsNegative() {
        final ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> itemController.searchItemsByNameOrDescription(1L, "a", -100, 10));

        assertTrue(exception.getMessage()
                .contains("Параметр запроса from, должен быть положительным числом или нулём."));
    }

/*    @Test
    public void tets() {
        ResponseEntity<Object> response = itemController.saveItem(1L, ItemDto.builder().name("asf").description("123123").available(true).build());
        System.out.println(response);
    }*/
}