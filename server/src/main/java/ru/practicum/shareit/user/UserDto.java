package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    // идентификатор:
    private Long id;

    // имя:
    private String name;

    // электронная почта:
    private String email;
}