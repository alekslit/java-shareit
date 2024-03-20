package ru.practicum.shareit.exception;

public class NotAvailableException extends RuntimeException {
    public static final String ITEM_NOT_AVAILABLE_MESSAGE = "Запрещённая операция. Невозможно забронировать предмет.";
    public static final String ITEM_NOT_AVAILABLE_ADVICE = "Предмет не доступен для аренды.";
    public static final String NOT_AVAILABLE_APPROVE_MESSAGE = "Невозможно подтвердить аренду повторно.";
    public static final String NOT_AVAILABLE_APPROVE_ADVICE = "Аренда уже подтверждена.";
    public static final String NOT_AVAILABLE_COMMENT_MESSAGE = "Невозможно оставить комментарий к предмету.";
    public static final String NOT_AVAILABLE_COMMENT_ADVICE = "Оставлять комментарии могут только пользователи, " +
            "которые брали предмет в аренду.";

    private final String adviceToUser;

    public NotAvailableException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}