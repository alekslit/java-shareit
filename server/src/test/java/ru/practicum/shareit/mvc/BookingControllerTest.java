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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private MockMvc mvc;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .user(User.builder()
                        .id(1L)
                        .build())
                .item(new Item())
                .build();
    }

    /*--- POST /bookings ---*/
    @Test
    public void saveBookingTest() throws Exception {
        when(bookingService.saveBooking(any(), any())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    /*--- PATCH /bookings/{bookingId} ---*/
    @Test
    public void approveBookingTest() throws Exception {
        when(bookingService.approveBooking(any(), any(), any())).thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("approved", "false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    /*--- GET /bookings/{bookingId} ---*/
    @Test
    public void getBookingById() throws Exception {
        when(bookingService.getBookingById(any())).thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    /*--- GET /bookings ---*/
    @Test
    public void getAllUserBookingsTest() throws Exception {
        when(bookingService.getAllUserBookings(1L, "ALL", 0, 10))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("state", "ALL")
                        .queryParam("from", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().name())));
    }

    /*--- GET /bookings/owner ---*/
    @Test
    public void getAllOwnerItemsBookingsTest() throws Exception {
        when(bookingService.getAllOwnerItemsBookings(1L, "ALL", 0, 10))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("state", "ALL")
                        .queryParam("from", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().name())));
    }
}