package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public static final String USER_NOT_FOUND_MESSAGE = "Пользователя с таким id не существует. id = ";
    public static final String USER_NOT_FOUND_ADVICE = "Пожалуйста проверьте корректность id пользователя.";
    public static final String ITEM_NOT_FOUND_MESSAGE = "Предмет с таким id не существует. id = ";
    public static final String ITEM_NOT_FOUND_ADVICE = "Пожалуйста проверьте корректность id предмета.";
    public static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирования предмета с таким id не существует. id = ";
    public static final String BOOKING_NOT_FOUND_ADVICE = "Пожалуйста проверьте корректность id бронирования предмета.";

    private final String adviceToUser;

    public NotFoundException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}