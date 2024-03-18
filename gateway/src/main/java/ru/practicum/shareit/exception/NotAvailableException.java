package ru.practicum.shareit.exception;

public class NotAvailableException extends RuntimeException {
    public static final String NOT_AVAILABLE_DATE_TIME_MESSAGE = "Недопустимый формат установленных дат аренды.";
    public static final String NOT_AVAILABLE_DATE_TIME_ADVICE = "Обратите внимание: " +
            "1. Дата начала или конца аренды не может быть пустой. " +
            "2. Даты начала и конца аренды не могут быть равны. " +
            "3. Даты начала или конца аренды не могут быть в прошлом. " +
            "4. Дата конца аренды не может быть до даты начала аренды.";
    public static final String NOT_AVAILABLE_STATE_MESSAGE = "Unknown state: ";
    public static final String NOT_AVAILABLE_STATE_ADVICE = "Такой фильтр state не поддерживается," +
            " выберите другой фильтр.";

    private final String adviceToUser;

    public NotAvailableException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}