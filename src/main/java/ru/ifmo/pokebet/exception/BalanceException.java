package ru.ifmo.pokebet.exception;

import org.springframework.http.HttpStatus;

public class BalanceException extends ApiException {
    public BalanceException(String message) {
        super(HttpStatus.PAYMENT_REQUIRED, message);
    }
}
