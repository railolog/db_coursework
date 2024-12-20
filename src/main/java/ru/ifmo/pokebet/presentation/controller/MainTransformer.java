package ru.ifmo.pokebet.presentation.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.domain.Trainer;
import ru.pokebet.openapi.model.BetListResponseTo;
import ru.pokebet.openapi.model.BetResponseTo;
import ru.pokebet.openapi.model.FightListResponseTo;
import ru.pokebet.openapi.model.FightResponseTo;
import ru.pokebet.openapi.model.LocationListTo;
import ru.pokebet.openapi.model.LocationTo;
import ru.pokebet.openapi.model.PokemonListTo;
import ru.pokebet.openapi.model.PokemonTo;
import ru.pokebet.openapi.model.TrainerResponseTo;

@Slf4j
@Component
public class MainTransformer {

    public TrainerResponseTo transform(Trainer trainer) {
        if (trainer == null) {
            log.warn("Skipping trainer transform, because trainer is null");
            return new TrainerResponseTo();
        }

        return new TrainerResponseTo()
                .id(trainer.getId())
                .name(trainer.getName());
    }

    public PokemonTo transform(Pokemon pokemon) {
        return new PokemonTo()
                .id(pokemon.getId())
                .name(pokemon.getName())
                .trainer(transform(pokemon.getTrainer()))
                .types(pokemon.getTypes());
    }

    public PokemonListTo transformPokemonList(List<Pokemon> pokemons) {
        return new PokemonListTo()
                .pokemons(
                        pokemons.stream()
                                .map(this::transform)
                                .toList()
                );
    }

    public LocationTo transform(Location location) {
        return new LocationTo()
                .id(location.getId())
                .name(location.getTitle())
                .description(location.getDescription());
    }

    public LocationListTo transformLocationList(List<Location> locations) {
        return new LocationListTo()
                .locations(
                        locations.stream()
                                .map(this::transform)
                                .toList()
                );
    }

    public FightResponseTo transform(Fight fight) {
        return new FightResponseTo()
                .id(fight.getId())
                .firstPokemon(this.transform(fight.getFirstPokemon()))
                .secondPokemon(this.transform(fight.getSecondPokemon()))
                .location(this.transform(fight.getLocation()))
                .coefficientFirst(fight.getCoefficientFirst())
                .coefficientSecond(fight.getCoefficientSecond())
                .firstWon(fight.getFirstWon())
                .isCompleted(fight.isCompleted());
    }

    public FightListResponseTo transformFightList(List<Fight> fights) {
        return new FightListResponseTo().fights(
                fights.stream()
                        .map(this::transform)
                        .toList()
        );
    }

    public BetResponseTo transform(Bet bet) {
        return new BetResponseTo()
                .id(bet.getId())
                .fight(transform(bet.getFight()))
                .credits(bet.getCredits())
                .firstPokemonChosen(bet.getChoice())
                .betCoef(bet.getBetCoef())
                .profit(bet.getIncome());
    }

    public BetListResponseTo transformBetList(List<Bet> bets) {
        return new BetListResponseTo().bets(
                bets.stream()
                        .map(this::transform)
                        .toList()
        );
    }
}
