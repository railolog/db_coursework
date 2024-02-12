package ru.ifmo.pokebet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.domain.Pokemon;

@Component
public class FightMapper implements RowMapper<Fight> {
    @Override
    public Fight mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Fight.builder()
                .id(rs.getInt("id"))
                .firstPokemon(
                        Pokemon.builder()
                                .id(rs.getInt("first_pokemon_id"))
                                .build()
                )
                .secondPokemon(
                        Pokemon.builder()
                                .id(rs.getInt("second_pokemon_id"))
                                .build()
                )
                .location(
                        Location.builder()
                                .id(rs.getInt("location_id"))
                                .build()
                )
                .isCompleted(rs.getBoolean("is_completed"))
                .coefficientFirst(rs.getDouble("coefficient"))
                .coefficientSecond(rs.getDouble("coefficient_second"))
                .firstWon(rs.getBoolean("first_won"))
                .build();
    }
}
