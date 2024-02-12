package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.domain.Pokemon;

@Service
public class FightRatioService {
    private static final double COMMISSION = 1.06;
    private static final Random rand = new Random();

    public List<Double> compute(Fight fight) {
        double firstPokemonSpec = specSum(fight.getFirstPokemon());
        double secondPokemonSpec = specSum(fight.getSecondPokemon());

        double specSum = firstPokemonSpec + secondPokemonSpec;

        return List.of(
                (1) / (firstPokemonSpec / specSum * COMMISSION),
                (1) / (secondPokemonSpec / specSum * COMMISSION)
        );
    }

    public boolean firstWon(Fight fight) {
        double prob = computeReal(fight);
        return rand.nextDouble() < prob;
    }

    private double computeReal(Fight fight) {
        double firstPokemonSpec = specSum(fight.getFirstPokemon());
        double secondPokemonSpec = specSum(fight.getSecondPokemon());

        double specSum = firstPokemonSpec + secondPokemonSpec;

        return specSum / firstPokemonSpec;
    }

    private long specSum(Pokemon p) {
        return p.getHp() + p.getAttack() + p.getDefense() + p.getSpeed();
    }
}
