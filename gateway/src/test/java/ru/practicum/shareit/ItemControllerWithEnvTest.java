package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemController;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerWithEnvTest {
    private final ItemController itemController;

    @Test
    public void getExceptionWhenFromIsNegative() {
        final ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> itemController.searchItemsByNameOrDescription(1L, "a", -100, 10));

        assertTrue(exception.getMessage()
                .contains("Параметр запроса from, должен быть положительным числом или нулём."));
    }
}