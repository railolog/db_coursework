package ru.ifmo.pokebet.presentation.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.domain.Fight;
import ru.ifmo.pokebet.service.FightQueryService;
import ru.pokebet.openapi.api.FightApi;
import ru.pokebet.openapi.model.FightListResponseTo;
import ru.pokebet.openapi.model.FightRequestTo;
import ru.pokebet.openapi.model.FightResponseTo;

@RestController
@RequiredArgsConstructor
public class FightController implements FightApi {
    private final FightQueryService fightQueryService;
    private final MainTransformer mainTransformer;
    private final UserService userService;

    @Override
    public ResponseEntity<FightResponseTo> createFight(FightRequestTo fightRequestTo) {
        User user = userService.getCurrentUser();
        Fight fight = fightQueryService.createFight(fightRequestTo, user);

        return ResponseEntity.ok(mainTransformer.transform(fight));
    }

    @Override
    public ResponseEntity<FightResponseTo> getFightById(Integer id) {
        Optional<Fight> fight = fightQueryService.getById(id);

        return fight
                .map(value -> ResponseEntity.ok(mainTransformer.transform(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FightResponseTo> startFight(Integer id) {
        Fight fight = fightQueryService.startFight(id, userService.getCurrentUser());

        return ResponseEntity.ok(mainTransformer.transform(fight));
    }

    @Override
    public ResponseEntity<FightListResponseTo> getMyFights() {
        List<Fight> fights = fightQueryService.getAll(userService.getCurrentUser());

        return ResponseEntity.ok(mainTransformer.transformFightList(fights));
    }
}
