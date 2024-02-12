package ru.ifmo.pokebet.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Fight {
    @EqualsAndHashCode.Include
    Integer id;
    Pokemon firstPokemon;
    Pokemon secondPokemon;
    Location location;
    Double coefficientFirst;
    Double coefficientSecond;
    boolean firstWon;
    boolean isCompleted;
}
