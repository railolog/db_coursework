package ru.ifmo.pokebet.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.repository.mapper.BetMapper;

@Repository
@RequiredArgsConstructor
public class BetRepository {
    private static final String SELECT_BASE_QUERY
            = " SELECT *"
            + " FROM bet";

    private static final String SELECT_BY_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE id = :id ";

    private static final String SELECT_BY_USER_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE user_id = :user_id ";

    private static final String INSERT_WITH_RETURNING
            = " INSERT INTO bet("
            + "   user_id,"
            + "   fight_id,"
            + "   credits,"
            + "   coef,"
            + "   choice"
            + " )"
            + " VALUES ("
            + "   :user_id,"
            + "   :fight_id,"
            + "   :credits,"
            + "   :coef,"
            + "   :choice"
            + " )"
            + " RETURNING * ";

    private static final String UPDATE
            = " UPDATE bet"
            + " SET "
            + "   income = :income "
            + " WHERE id = :id ";

    private static final String UPDATE_WITH_RETURNING
            = UPDATE
            + " RETURNING * ";

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final BetMapper betMapper;

    public Optional<Bet> findById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SELECT_BY_ID_QUERY,
                            Map.of("id", id),
                            betMapper
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Bet> findAll(int userId) {
        return jdbcTemplate.query(
                SELECT_BY_USER_ID_QUERY,
                Map.of("user_id", userId),
                betMapper
        );
    }

    public Bet save(Bet bet, User user) {
        return jdbcTemplate.queryForObject(
                INSERT_WITH_RETURNING,
                buildParametersToUpdateQuery(bet, user),
                betMapper
        );
    }

    public Bet update(Bet bet) {
        return jdbcTemplate.queryForObject(
                UPDATE_WITH_RETURNING,
                Map.of(
                        "id", bet.getId(),
                        "income", bet.getIncome()
                ),
                betMapper
        );
    }

    private MapSqlParameterSource buildParametersToUpdateQuery(Bet bet, User user) {
        return new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("fight_id", bet.getFight().getId())
                .addValue("credits", bet.getCredits())
                .addValue("coef", bet.getBetCoef())
                .addValue("choice", bet.getChoice());
    }
}
