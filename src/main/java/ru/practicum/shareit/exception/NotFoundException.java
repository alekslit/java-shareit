package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public final static String USER_NOT_FOUND_MESSAGE = "Пользователя с таким id не существует. id = ";
    public final static String USER_NOT_FOUND_ADVICE = "Пожалуйста проверьте корректность id пользователя.";

    private final String adviceToUser;

    public NotFoundException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}