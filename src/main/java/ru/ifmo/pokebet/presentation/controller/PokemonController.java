package ru.ifmo.pokebet.presentation.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.exception.NotAdminException;
import ru.ifmo.pokebet.service.PokemonQueryService;
import ru.pokebet.openapi.api.PokemonApi;
import ru.pokebet.openapi.model.CreatePokemonRequestTo;
import ru.pokebet.openapi.model.PokemonTo;
import ru.pokebet.openapi.model.PokemonListTo;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {
    private final UserService userService;
    private final PokemonQueryService pokemonQueryService;
    private final MainTransformer mainTransformer;

    public ResponseEntity<PokemonTo> getPokemon(Integer id) {
        Optional<Pokemon> pokemonOpt = pokemonQueryService.findById(id);

        return pokemonOpt
                .map(mainTransformer::transform)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    public ResponseEntity<PokemonListTo> getAllPokemons() {
        return ResponseEntity.ok(mainTransformer.transformPokemonList(pokemonQueryService.findAll()));
    }

    @Override
    public ResponseEntity<PokemonTo> createPokemon(CreatePokemonRequestTo createPokemonRequestTo) {
        User user = userService.getCurrentUser();
        if (!user.isAdmin()) {
            throw new NotAdminException();
        }

        return ResponseEntity.ok(
                mainTransformer.transform(pokemonQueryService.save(createPokemonRequestTo))
        );
    }
}
