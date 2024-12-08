package ru.ifmo.pokebet.auth.repo;

import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.ifmo.pokebet.auth.model.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static final String SELECT_BASE_QUERY
            = " SELECT *"
            + " FROM \"user\"";

    private static final String SELECT_BY_NAME_QUERY
            = SELECT_BASE_QUERY
            + " WHERE username = :username";

    private static final String INSERT_WITH_RETURNING
            = " INSERT INTO \"user\"("
            + "   username,"
            + "   password,"
            + "   role"
            + " )"
            + " VALUES ("
            + "   :username,"
            + "   :password,"
            + "   :role"
            + " )"
            + " RETURNING * ";

    private static final String UPDATE
            = " UPDATE \"user\""
            + " SET  "
            + "   username = :username, "
            + "   balance = :balance "
            + " WHERE id = :id ";

    private static final String UPDATE_WITH_RETURNING
            = UPDATE
            + " RETURNING * ";

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SELECT_BY_NAME_QUERY,
                            Map.of("username", username),
                            userMapper
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public User save(User user) {
        return jdbcTemplate.queryForObject(
                INSERT_WITH_RETURNING,
                buildParametersToInsertQuery(user),
                userMapper
        );
    }

    public User update(User user) {
        return jdbcTemplate.queryForObject(
                UPDATE_WITH_RETURNING,
                buildParametersToUpdateQuery(user),
                userMapper
        );
    }

    private MapSqlParameterSource buildParametersToInsertQuery(User user) {
        return new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword())
                .addValue("balance", user.getBalance())
                .addValue("role", user.getRole().name());
    }

    private MapSqlParameterSource buildParametersToUpdateQuery(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword())
                .addValue("balance", user.getBalance())
                .addValue("role", user.getRole().name());
    }
}
