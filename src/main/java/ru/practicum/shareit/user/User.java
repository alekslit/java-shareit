package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class User {
    // идентификатор пользователя:
    private Long id;
    // имя пользователя:
    private String name;

    //электронная почта:
    @NotNull(message = "Адрес электронной почты не может быть пустым")
    @Email(regexp = "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+@"
            + "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+[\\\\.]{1}[a-z]{2,10}",
            message = "Некорректный адресс электронной почты: ${validatedValue}")
    private String email;
}