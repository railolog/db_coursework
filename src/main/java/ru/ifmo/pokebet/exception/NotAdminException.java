package ru.ifmo.pokebet.exception;

import org.springframework.http.HttpStatus;

public class NotAdminException extends ApiException {
    public NotAdminException() {
        super(HttpStatus.FORBIDDEN, "Not enough permissions");
    }
}
