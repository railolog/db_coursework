package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BetManagementService {
    private final FightQueryService fightQueryService;
    private final FightRatioService fightRatioService;
    private final BetQueryService betQueryService;
    private final UserService userService;

    @Transactional
    public void startFight(int id, User user) {
        Fight fight = fightQueryService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Fight with id [" + id + "] doesn't exist"));

        boolean firstWon = fightRatioService.firstWon(fight);

        List<Bet> bets = betQueryService.getAll(user).stream()
                .filter(bet -> Objects.equals(bet.getFight().getId(), fight.getId()))
                .toList();

        bets.forEach(bet -> bet.setIncome(income(bet, firstWon)));
        bets.forEach(betQueryService::update);

        double income = bets.stream()
                .mapToDouble(bet -> income(bet, firstWon))
                .sum();

        fight.setCompleted(true);
        fight.setFirstWon(firstWon);
        Fight updatedFight = fightQueryService.update(fight, user);

        user.setBalance(user.getBalance() + income);
        userService.update(user);
    }

    private double income(Bet bet, boolean firstWon) {
        if (firstWon && bet.getChoice() || !firstWon && !bet.getChoice()) {
            return bet.getBetCoef() * bet.getCredits();
        }

        return 0;
    }
}
