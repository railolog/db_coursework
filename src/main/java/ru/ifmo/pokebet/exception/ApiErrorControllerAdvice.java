package ru.ifmo.pokebet.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.pokebet.openapi.model.ErrorResponseTo;

@ControllerAdvice
public class ApiErrorControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseTo> handleApiErrorException(ApiException apiError) {
        return new ResponseEntity<>(
                new ErrorResponseTo()
                        .code(apiError.getHttpStatus().value())
                        .message(apiError.getMessage()),
                apiError.getHttpStatus()
        );
    }
}
