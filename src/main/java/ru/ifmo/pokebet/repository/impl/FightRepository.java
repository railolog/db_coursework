package ru.ifmo.pokebet.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.repository.mapper.FightMapper;

@Repository
@RequiredArgsConstructor
public class FightRepository {
    private static final String SELECT_BASE_QUERY
            = " SELECT * "
            + " FROM fight ";

    private static final String ORDER_BY_ID = " order by id ";

    private static final String SELECT_BY_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE id = :id ";

    private static final String SELECT_BY_USER_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE user_id = :user_id "
            + ORDER_BY_ID;

    private static final String INSERT_WITH_RETURNING
            = " INSERT INTO fight("
            + "   first_pokemon_id,"
            + "   second_pokemon_id,"
            + "   location_id,"
            + "   is_completed,"
            + "   coefficient,"
            + "   coefficient_second,"
            + "   first_won,"
            + "   user_id"
            + " )"
            + " VALUES ("
            + "   :first_pokemon_id,"
            + "   :second_pokemon_id,"
            + "   :location_id,"
            + "   :is_completed,"
            + "   :coefficient,"
            + "   :coefficient_second,"
            + "   :first_won,"
            + "   :user_id"
            + " )"
            + " RETURNING * ";

    private static final String UPDATE
            = " UPDATE fight"
            + " SET "
            + "   is_completed = :is_completed, "
            + "   first_won = :first_won "
            + " WHERE id = :id ";

    private static final String UPDATE_WITH_RETURNING
            = UPDATE
            + " RETURNING * ";

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final FightMapper fightMapper;

    public Optional<Fight> findById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SELECT_BY_ID_QUERY,
                            Map.of("id", id),
                            fightMapper
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Fight> findAll(int userId) {
        return jdbcTemplate.query(
                SELECT_BY_USER_ID_QUERY,
                Map.of("user_id", userId),
                fightMapper
        );
    }

    public Fight save(Fight fight, User user) {
        return jdbcTemplate.queryForObject(
                INSERT_WITH_RETURNING,
                buildParametersToInsertQuery(fight, user),
                fightMapper
        );
    }

    public Fight update(Fight fight, User user) {
        return jdbcTemplate.queryForObject(
                UPDATE_WITH_RETURNING,
                buildParametersToUpdateQuery(fight, user),
                fightMapper
        );
    }

    private MapSqlParameterSource buildParametersToInsertQuery(Fight fight, User user) {
        return new MapSqlParameterSource()
                .addValue("first_pokemon_id", fight.getFirstPokemon().getId())
                .addValue("second_pokemon_id", fight.getSecondPokemon().getId())
                .addValue("location_id", fight.getLocation().getId())
                .addValue("is_completed", fight.isCompleted())
                .addValue("coefficient", fight.getCoefficientFirst())
                .addValue("coefficient_second", fight.getCoefficientSecond())
                .addValue("first_won", fight.isFirstWon())
                .addValue("user_id", user.getId());
    }

    private MapSqlParameterSource buildParametersToUpdateQuery(Fight fight, User user) {
        return new MapSqlParameterSource()
                .addValue("id", fight.getId())
                .addValue("first_pokemon_id", fight.getFirstPokemon().getId())
                .addValue("second_pokemon_id", fight.getSecondPokemon().getId())
                .addValue("location_id", fight.getLocation().getId())
                .addValue("is_completed", fight.isCompleted())
                .addValue("coefficient", fight.getCoefficientFirst())
                .addValue("coefficient_second", fight.getCoefficientSecond())
                .addValue("first_won", fight.isFirstWon())
                .addValue("user_id", user.getId());
    }
}
