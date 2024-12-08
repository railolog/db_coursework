package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.dockerjava.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.repository.BetRepository;
import ru.ifmo.pokebet.repository.FightRepository;
import ru.pokebet.openapi.model.FightRequestTo;

@Service
@RequiredArgsConstructor
public class FightQueryService {
    private final FightRepository fightRepository;
    private final PokemonQueryService pokemonQueryService;
    private final LocationQueryService locationQueryService;
    private final FightRatioService fightRatioService;
    private final BetRepository betRepository;
    private final UserService userService;

    public Optional<Fight> getById(int id) {
        Optional<Fight> fightOpt = fightRepository.findById(id);

        return fightOpt.map(this::enrich);
    }

    public List<Fight> getAll(User user) {
        return fightRepository.findAll(user.getId()).stream()
                .map(this::enrich)
                .toList();
    }

    @Transactional
    public Fight createFight(FightRequestTo fightRequestTo, User user) {
        if (!pokemonQueryService.existsById(fightRequestTo.getFirstPokemonId()) ||
                !pokemonQueryService.existsById(fightRequestTo.getSecondPokemonId())
        ) {
            throw new NotFoundException("pokemon doesn't exist");
        }

        if (fightRequestTo.getFirstPokemonId().equals(fightRequestTo.getSecondPokemonId())) {
            throw new RuntimeException("can't use same pokemons");
        }

        if (!locationQueryService.existsById(fightRequestTo.getLocationId())) {
            throw new NotFoundException("location doesn't exist");
        }

        Fight fight = enrich(new Fight(
                null,
                Pokemon.builder().id(fightRequestTo.getFirstPokemonId()).build(),
                Pokemon.builder().id(fightRequestTo.getSecondPokemonId()).build(),
                Location.builder().id(fightRequestTo.getLocationId()).build(),
                null,
                null,
                false,
                false
        ));

        List<Double> ratio = fightRatioService.compute(fight);
        fight.setCoefficientFirst(ratio.get(0));
        fight.setCoefficientSecond(ratio.get(1));

        return fightRepository.save(
                fight,
                user
        );
    }

    @Transactional
    public Fight startFight(int id, User user) {
        Fight fight = enrich(fightRepository.findById(id).orElseThrow());

        boolean firstWon = fightRatioService.firstWon(fight);

        List<Bet> bets = betRepository.findAll(user.getId()).stream()
                .filter(bet -> Objects.equals(bet.getFight().getId(), fight.getId()))
                .toList();

        double income = bets.stream()
                .mapToDouble(bet -> income(bet, fight, firstWon))
                .sum();

        fight.setCompleted(true);
        fight.setFirstWon(firstWon);
        Fight updatedFight = fightRepository.update(fight, user);

        user.setBalance(user.getBalance() + income);
        userService.update(user);

        return enrich(updatedFight);
    }

    public boolean existsById(int id) {
        return fightRepository.findById(id).isPresent();
    }

    private Fight enrich(Fight fight) {
        Pokemon firstPokemon = pokemonQueryService.findById(fight.getFirstPokemon().getId()).orElseThrow();
        Pokemon secondPokemon = pokemonQueryService.findById(fight.getSecondPokemon().getId()).orElseThrow();
        Location location = locationQueryService.getById(fight.getLocation().getId()).orElseThrow();

        fight.setFirstPokemon(firstPokemon);
        fight.setSecondPokemon(secondPokemon);
        fight.setLocation(location);

        return fight;
    }

    private double income(Bet bet, Fight fight, boolean firstWon) {
        if (firstWon && bet.getChoice()) {
                return fight.getCoefficientFirst() * bet.getCredits();
        }

        if (!firstWon && !bet.getChoice()) {
            return fight.getCoefficientSecond() * bet.getCredits();
        }

        return 0;
    }
}
