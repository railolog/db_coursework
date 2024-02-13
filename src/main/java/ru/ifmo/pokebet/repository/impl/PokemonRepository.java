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
            = " SELECT pokemon.id as id, hp, attack, previous_stage, trainer_id, defense, speed,"
            + " pokemon.name as name, t.id as tid, t.name as tname"
            + " FROM pokemon"
            + " join trainer t on pokemon.trainer_id = t.id";

    private static final String SELECT_BY_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE pokemon.id = :id ";

    private static final String SELECT_TYPE_BY_ID_QUERY
            = """
                 select pt.type as type from pokemon
                 join has_type ht on pokemon.id = ht.pokemon_id
                 join pokemon_type pt on ht.type_id = pt.id
                 where pokemon.id=:id
                 """;

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

    public List<String> findTypesById(int id) {
        return jdbcTemplate.query(
                SELECT_TYPE_BY_ID_QUERY,
                Map.of("id", id),
                ((rs, rowNum) -> rs.getString("type"))
        );
    }
}
