package ru.ifmo.pokebet.exception;

import org.springframework.http.HttpStatus;

public class SamePokemonUsedException extends ApiException {
    public SamePokemonUsedException() {
        super(HttpStatus.CONFLICT, "Can't use same pokemons");
    }
}
