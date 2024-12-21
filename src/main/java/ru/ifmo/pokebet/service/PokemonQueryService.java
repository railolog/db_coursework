package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.repository.PokemonRepository;
import ru.pokebet.openapi.model.CreatePokemonRequestTo;

@Service
@RequiredArgsConstructor
public class PokemonQueryService {
    private final PokemonRepository pokemonRepository;

    public Optional<Pokemon> findById(int id) {
        return pokemonRepository.findById(id).map(this::enrich);
    }

    public List<Pokemon> findAll() {
        return pokemonRepository.findAll().stream()
                .map(this::enrich)
                .toList();
    }

    @Transactional
    public Pokemon save(CreatePokemonRequestTo pokemon) {
        Pokemon build = Pokemon.builder()
                .name(pokemon.getName())
                .types(pokemon.getTypes())
                .trainerId(pokemon.getTrainerId())
                .build();

        return enrich(pokemonRepository.save(build));
    }

    public boolean existsById(int id) {
        return pokemonRepository.findById(id).isPresent();
    }

    private Pokemon enrich(Pokemon pokemon) {
        List<String> typesById = pokemonRepository.findTypesById(pokemon.getId());
        pokemon.setTypes(typesById);
        return pokemon;
    }
}
