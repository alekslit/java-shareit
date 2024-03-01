package ru.practicum.shareit.exception;

public class ForbiddenOperationException extends RuntimeException {
    public static final String NOT_OWNER_ITEM_MESSAGE = "Запрещённая операция. " +
            "Нет доступа к изменению данных о предмете у пользователя с id = ";
    public static final String NOT_OWNER_ITEM_ADVICE = "Данные о предмете может менять только его владелец.";
    public static final String NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_MESSAGE = "Запрещённая операция. " +
            "Нет доступа к просмотру данных о бронировании предмета у пользователя с id = ";
    public static final String NOT_OWNER_ITEM_OR_BOOKING_AUTHOR_ADVICE = "Данные о бронировании предмета может " +
            "просматривать только его владелец, или автор бронирования предмета.";
    public static final String NOT_ACCESS_TO_APPROVE_MESSAGE = "Запрещённая операция. " +
            "Нет доступа к изменению статуса бронирования предмета у пользователя с id = ";
    public static final String NOT_ACCESS_TO_APPROVE_ADVICE = "Статус бронирования предмета может менять " +
            "только его владелец.";
    public static final String BOOKER_IS_OWNER_MESSAGE = "Запрещённая операция. " +
            "Нет доступа к бронированию предмета у пользователя с id = ";
    public static final String BOOKER_IS_OWNER_ADVICE = "Владелец предмета не может брать его в аренду.";

    private final String adviceToUser;

    public ForbiddenOperationException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}