package ru.ifmo.pokebet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.domain.Fight;

@Component
public class BetMapper implements RowMapper<Bet> {
    @Override
    public Bet mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Bet.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .fight(Fight.builder().id(rs.getInt("fight_id")).build())
                .credits(rs.getInt("credits"))
                .choice(rs.getBoolean("choice"))
                .build();
    }
}
