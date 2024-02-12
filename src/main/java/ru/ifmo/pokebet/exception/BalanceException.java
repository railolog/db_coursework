package ru.ifmo.pokebet.exception;

public class BalanceException extends RuntimeException{
    public BalanceException(String message) {
        super(message);
    }
}
