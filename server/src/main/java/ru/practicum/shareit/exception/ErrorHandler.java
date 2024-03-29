package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {
    /*------Обработчики для статуса 400 (Bad request)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotAvailableException(final NotAvailableException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .adviceToUser(e.getAdviceToUser())
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 404 (Not found)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleForbiddenOperationException(final ForbiddenOperationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .adviceToUser(e.getAdviceToUser())
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .adviceToUser(e.getAdviceToUser())
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 409 (Conflict)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final AlreadyExistException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .adviceToUser(e.getAdviceToUser())
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }

    /*------Обработчики для статуса 500 (Internal server error)------*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Произошла непредвиденная ошибка.")
                .adviceToUser("Пожалуйста, обратитесь в службу технической поддержки.")
                .build();
        log.debug("{}: {}", e.getClass().getSimpleName(), e.getMessage());

        return errorResponse;
    }
}