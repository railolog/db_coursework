package ru.ifmo.pokebet.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.repository.mapper.PokemonMapper;

@Repository
@RequiredArgsConstructor
public class PokemonRepository {
    private static final String SELECT_BASE_QUERY
            = " SELECT *"
            + " FROM pokemon";

    private static final String SELECT_BY_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE id = :id ";

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final PokemonMapper pokemonMapper;

    public Optional<Pokemon> findById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SELECT_BY_ID_QUERY,
                            Map.of("id", id),
                            pokemonMapper
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Pokemon> findAll() {
        return jdbcTemplate.query(
                SELECT_BASE_QUERY,
                pokemonMapper
        );
    }
}
