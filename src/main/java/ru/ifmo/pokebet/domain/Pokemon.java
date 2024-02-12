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
public class Pokemon {
    @EqualsAndHashCode.Include
    Integer id;
    Integer hp;
    Integer attack;
    Integer previousStage;
    Integer trainerId;
    Integer defense;
    Integer speed;
    String name;
}
