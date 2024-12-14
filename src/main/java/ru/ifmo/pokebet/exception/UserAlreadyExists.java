package ru.ifmo.pokebet.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends ApiException {
    public UserAlreadyExists(String username) {
        super(HttpStatus.CONFLICT, "User with login [" + username + "] already exists");
    }
}
