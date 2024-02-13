package ru.ifmo.pokebet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.domain.Trainer;

@Component
public class PokemonMapper implements RowMapper<Pokemon> {
    @Override
    public Pokemon mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Pokemon.builder()
                .id(rs.getInt("id"))
                .hp(rs.getInt("hp"))
                .attack(rs.getInt("attack"))
                .previousStage(rs.getInt("previous_stage"))
                .trainerId(rs.getInt("trainer_id"))
                .defense(rs.getInt("defense"))
                .speed(rs.getInt("speed"))
                .name(rs.getString("name"))
                .trainer(
                        Trainer.builder()
                                .id(rs.getInt("trainer_id"))
                                .name(rs.getString("tname"))
                                .build()
                )
                .build();
    }
}
