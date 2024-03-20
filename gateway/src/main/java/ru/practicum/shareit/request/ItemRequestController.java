package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> saveItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Creating itemRequest {}, userId={}", itemRequestDto, userId);

        return client.saveItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get itemRequest userId={}", userId);

        return client.getAllItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Параметр запроса from, должен быть " +
                    "положительным числом или нулём.") int from,
            @RequestParam(defaultValue = "10") @Positive(message = "Параметр запроса size, должен быть " +
                    "положительным числом.") int size) {
        log.info("Get itemRequest with userId={}, from={}, size={}", userId, from, size);

        return client.getAllItemRequestOtherUsers(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long requestId) {
        log.info("Get itemRequest, userId={}, requestId={}", userId, requestId);

        return client.getItemRequestById(userId, requestId);
    }
}