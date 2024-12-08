package ru.ifmo.pokebet.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.repository.mapper.LocationMapper;

@Repository
@RequiredArgsConstructor
public class LocationRepository {
    private static final String SELECT_BASE_QUERY
            = " SELECT *"
            + " FROM location";

    private static final String SELECT_BY_ID_QUERY
            = SELECT_BASE_QUERY
            + " WHERE id = :id ";

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final LocationMapper locationMapper;

    public List<Location> findAll() {
        return jdbcTemplate.query(
                SELECT_BASE_QUERY,
                locationMapper
        );
    }

    public Optional<Location> findById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            SELECT_BY_ID_QUERY,
                            Map.of("id", id),
                            locationMapper
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
