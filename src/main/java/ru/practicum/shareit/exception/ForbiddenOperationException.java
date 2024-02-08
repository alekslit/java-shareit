package ru.practicum.shareit.exception;

public class ForbiddenOperationException extends RuntimeException {
    public final static String NOT_OWNER_ITEM_MESSAGE = "Запрещённая операция. " +
            "Нет доступа к изменению данных о предмете у пользователя с id = ";
    public final static String NOT_OWNER_ITEM_ADVICE = "Данные о предмете может менять только его владелец.";

    private final String adviceToUser;

    public ForbiddenOperationException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}