package ru.practicum.shareit.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_ADVICE;
import static ru.practicum.shareit.exception.ForbiddenOperationException.NOT_OWNER_ITEM_MESSAGE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_COMMENT_ADVICE;
import static ru.practicum.shareit.exception.NotAvailableException.NOT_AVAILABLE_COMMENT_MESSAGE;
import static ru.practicum.shareit.exception.NotFoundException.ITEM_NOT_FOUND_ADVICE;
import static ru.practicum.shareit.exception.NotFoundException.ITEM_NOT_FOUND_MESSAGE;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemController itemController;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDto item;
    private CommentDto comment;

    public void init() {
        item = ItemDto.builder()
                .name("test1")
                .description("test1")
                .available(true)
                .build();
        comment = CommentDto.builder()
                .text("test2")
                .build();
    }

    @BeforeEach
    public void setUp() {
        init();
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

    /*--- POST /items/1/comment ---*/
    @Test
    public void getErrorAndBadRequestWhenNotAvailableException() throws Exception {
        when(itemController.saveComment(any(), any(), any()))
                .thenThrow(new NotAvailableException(NOT_AVAILABLE_COMMENT_MESSAGE, NOT_AVAILABLE_COMMENT_ADVICE));

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        is(NOT_AVAILABLE_COMMENT_MESSAGE)))
                .andExpect(jsonPath("$.adviceToUser",
                        is(NOT_AVAILABLE_COMMENT_ADVICE)));
    }

    /*--- PATCH /items/1 ---*/
    @Test
    public void getErrorAndNotFoundWhenForbiddenOperationException() throws Exception {
        when(itemController.updateItem(any(), any(), any()))
                .thenThrow(new ForbiddenOperationException(NOT_OWNER_ITEM_MESSAGE + 1, NOT_OWNER_ITEM_ADVICE));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error",
                        is(NOT_OWNER_ITEM_MESSAGE + 1)))
                .andExpect(jsonPath("$.adviceToUser",
                        is(NOT_OWNER_ITEM_ADVICE)));
    }

    /*--- GET /items/1 ---*/
    @Test
    public void getErrorAndNotFoundWhenNotFoundException() throws Exception {
        when(itemController.getItemById(any(), any()))
                .thenThrow(new NotFoundException(ITEM_NOT_FOUND_MESSAGE + 1, ITEM_NOT_FOUND_ADVICE));

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error",
                        is(ITEM_NOT_FOUND_MESSAGE + 1)))
                .andExpect(jsonPath("$.adviceToUser",
                        is(ITEM_NOT_FOUND_ADVICE)));
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