package ru.practicum.shareit.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private ItemRequestService itemRequestService;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private MockMvc mvc;
    private ItemRequest request;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();
        request = ItemRequest.builder()
                .id(1L)
                .description("test1")
                .build();
    }

    /*--- POST /requests ---*/
    @Test
    public void saveItemRequestTest() throws Exception {
        when(itemRequestService.saveItemRequest(any(), any())).thenReturn(request);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    /*--- GET /requests ---*/
    @Test
    public void getAllItemRequestByUserIdTest() throws Exception {
        when(itemRequestService.findAllByUserId(any())).thenReturn(List.of(request));
        when(itemRequestService.addItemToItemRequest(anyList()))
                .thenReturn(List.of(ItemRequestMapper.mapToItemRequestDto(request)));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())));
    }

    /*--- GET /requests/all ---*/
    @Test
    public void getAllItemRequestOtherUsersTest() throws Exception {
        when(itemRequestService.findAllFromOtherUsers(1L, 0, 10)).thenReturn(List.of(request));
        when(itemRequestService.addItemToItemRequest(anyList()))
                .thenReturn(List.of(ItemRequestMapper.mapToItemRequestDto(request)));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())));
    }

    /*--- GET /requests/{requestId} ---*/
    @Test
    public void getItemRequestByIdTest() throws Exception {
        when(itemRequestService.getItemRequestById(1L, 1L)).thenReturn(request);
        when(itemRequestService.addItemToItemRequest(ItemRequestMapper.mapToItemRequestDto(request)))
                .thenReturn(ItemRequestMapper.mapToItemRequestDto(request));

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }
}