package ru.ifmo.pokebet.presentation.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Bet;
import ru.ifmo.pokebet.service.BetQueryService;
import ru.pokebet.openapi.api.BetApi;
import ru.pokebet.openapi.model.BetListResponseTo;
import ru.pokebet.openapi.model.BetRequestTo;
import ru.pokebet.openapi.model.BetResponseTo;

@RestController
@RequiredArgsConstructor
public class BetController implements BetApi {
    private final BetQueryService betQueryService;
    private final MainTransformer mainTransformer;
    private final UserService userService;

    @Override
    public ResponseEntity<BetResponseTo> createBet(BetRequestTo betRequestTo) {
        User user = userService.getCurrentUser();
        Bet bet = betQueryService.createBet(betRequestTo, user);

        return ResponseEntity.ok(mainTransformer.transform(bet));
    }

    @Override
    public ResponseEntity<BetResponseTo> getBetById(Integer id) {
        Optional<Bet> betOpt = betQueryService.getById(id);

        return betOpt
                .map(bet -> ResponseEntity.ok(mainTransformer.transform(bet)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<BetListResponseTo> getMyBets() {
        User user = userService.getCurrentUser();
        List<Bet> bets = betQueryService.getAll(user);

        return ResponseEntity.ok(mainTransformer.transformBetList(bets));
    }
}
