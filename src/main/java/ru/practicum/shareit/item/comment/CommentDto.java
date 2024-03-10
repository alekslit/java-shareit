package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;

    @NotBlank(message = "Текст отзыва не может быть пустым.")
    private String text;

    private String authorName;

    private LocalDateTime created;
}