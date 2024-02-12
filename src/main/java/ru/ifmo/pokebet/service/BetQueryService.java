package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Optional;

import com.github.dockerjava.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.exception.BalanceException;
import ru.ifmo.pokebet.repository.impl.BetRepository;
import ru.pokebet.openapi.model.BetRequestTo;

@Service
@RequiredArgsConstructor
public class BetQueryService {
    private final BetRepository betRepository;
    private final FightQueryService fightQueryService;
    private final UserService userService;

    public Optional<Bet> getById(int id) {
        return betRepository.findById(id)
                .map(this::enrich);
    }

    public List<Bet> getAll(User user) {
        return betRepository.findAll(user.getId()).stream()
                .map(this::enrich)
                .toList();
    }

    public Bet createBet(BetRequestTo betRequestTo, User user) {
        if (!fightQueryService.existsById(betRequestTo.getFightId())) {
            throw new NotFoundException("fight doesn't exist");
        }

        double balance = user.getBalance();
        if (balance < betRequestTo.getCredits()) {
            throw new BalanceException("Not enough money");
        }

        user.setBalance(balance - betRequestTo.getCredits());
        user = userService.update(user);

        Bet bet = Bet.builder()
                .userId(user.getId())
                .fight(Fight.builder().id(betRequestTo.getFightId()).build())
                .credits(betRequestTo.getCredits())
                .choice(betRequestTo.getFirstPokemonChosen())
                .build();

        return enrich(betRepository.save(bet, user));
    }

    private Bet enrich(Bet bet) {
        Fight fight = fightQueryService.getById(bet.getFight().getId()).orElseThrow();
        bet.setFight(fight);

        return bet;
    }
}
