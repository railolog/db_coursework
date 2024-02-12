package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.repository.impl.PokemonRepository;

@Service
@RequiredArgsConstructor
public class PokemonQueryService {
    private final PokemonRepository pokemonRepository;

    public Optional<Pokemon> findById(int id) {
        return pokemonRepository.findById(id);
    }

    public List<Pokemon> findAll() {
        return pokemonRepository.findAll();
    }

    public boolean existsById(int id) {
        return pokemonRepository.findById(id).isPresent();
    }
}
