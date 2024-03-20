package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}