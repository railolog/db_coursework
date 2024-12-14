package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.domain.Pokemon;
import ru.ifmo.pokebet.exception.NotFoundException;
import ru.ifmo.pokebet.exception.SamePokemonUsedException;
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

    public Optional<Fight> findById(int id) {
        Optional<Fight> fightOpt = fightRepository.findById(id);

        return fightOpt.map(this::enrich);
    }

    public List<Fight> getAll(User user) {
        return fightRepository.findAll(user.getId()).stream()
                .map(this::enrich)
                .toList();
    }

    @Transactional
    public Fight update(Fight fight, User user) {
        return fightRepository.update(fight, user);
    }

    @Transactional
    public Fight createFight(FightRequestTo fightRequestTo, User user) {
        Integer firstPokemonId = fightRequestTo.getFirstPokemonId();
        Integer secondPokemonId = fightRequestTo.getSecondPokemonId();
        if (!pokemonQueryService.existsById(firstPokemonId) || !pokemonQueryService.existsById(secondPokemonId)
        ) {
            throw new NotFoundException(String.format(
                    "Pokemons with ids [%d] and [%d] not found",
                    firstPokemonId,
                    secondPokemonId
            ));
        }

        if (firstPokemonId.equals(secondPokemonId)) {
            throw new SamePokemonUsedException();
        }

        if (!locationQueryService.existsById(fightRequestTo.getLocationId())) {
            throw new NotFoundException("Location with id [" + fightRequestTo.getLocationId() + "] doesn't exist");
        }

        Fight fight = enrich(new Fight(
                null,
                Pokemon.builder().id(firstPokemonId).build(),
                Pokemon.builder().id(secondPokemonId).build(),
                Location.builder().id(fightRequestTo.getLocationId()).build(),
                null,
                null,
                null,
                false
        ));

        List<Double> ratio = fightRatioService.compute(fight);
        fight.setCoefficientFirst(ratio.get(0));
        fight.setCoefficientSecond(ratio.get(1));

        return enrich(fightRepository.save(
                fight,
                user
        ));
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
}
