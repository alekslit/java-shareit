package ru.practicum.shareit.exception;

public class AlreadyExistException extends RuntimeException {
    public final static String DUPLICATE_EMAIL_MESSAGE = "Пользователь с таким email уже существует. email = ";
    public final static String DUPLICATE_EMAIL_ADVICE = "Пожалуйста, замените email.";

    private final String adviceToUser;

    public AlreadyExistException(String message, String adviceToUser) {
        super(message);
        this.adviceToUser = adviceToUser;
    }

    public String getAdviceToUser() {
        return adviceToUser;
    }
}