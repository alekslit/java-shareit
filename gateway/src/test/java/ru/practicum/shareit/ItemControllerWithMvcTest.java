package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerWithMvcTest {
    @MockBean
    private ItemController itemController;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDto item;

    @BeforeEach
    public void setUp() {
        item = ItemDto.builder()
                .name("test1")
                .description("test1")
                .available(true)
                .build();
    }

    /*--- POST /items ---*/
    @Test
    public void getErrorAndBadRequestWhenSaveItemWithoutDescription() throws Exception {
        item = item.toBuilder()
                .description(null)
                .build();

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        is("Ошибка валидации данных из запроса.")))
                .andExpect(jsonPath("$.adviceToUser",
                        is("Описание предмета (description) не может быть пустым")));
    }

    /*--- GET /items ---*/
    @Test
    public void getErrorAndBadRequestWithoutRequestHeader() throws Exception {
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        is("Отсутствует заголовок запроса: X-Sharer-User-Id")))
                .andExpect(jsonPath("$.adviceToUser",
                        is("Пожалуйста, добавьте заголовок запроса.")));
    }

    /*--- GET /items/search ---*/
    @Test
    public void getErrorAndInternalServerErrorWhenFromIsB() throws Exception {
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("text", "a")
                        .queryParam("from", "b")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error",
                        is("Произошла непредвиденная ошибка.")))
                .andExpect(jsonPath("$.adviceToUser",
                        is("Пожалуйста, обратитесь в службу технической поддержки.")));
    }
}