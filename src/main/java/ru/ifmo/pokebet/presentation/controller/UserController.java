package ru.ifmo.pokebet.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.model.Role;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;
import ru.ifmo.pokebet.exception.NotAdminException;
import ru.pokebet.openapi.api.UserApi;
import ru.pokebet.openapi.model.PaymentRequestTo;
import ru.pokebet.openapi.model.UserBalanceResponseTo;
import ru.pokebet.openapi.model.UserInfoResponseTo;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoResponseTo> getMeInfo() {
        return ResponseEntity.ok(transform(userService.getCurrentUser()));
    }

    @Override
    public ResponseEntity<Void> increaseBalance(PaymentRequestTo paymentRequestTo) {
        User user = userService.getCurrentUser();
        user.setBalance(user.getBalance() + paymentRequestTo.getAmount());
        userService.update(user);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserBalanceResponseTo> getBalance() {
        return ResponseEntity.ok(
                new UserBalanceResponseTo()
                        .balance(userService.getCurrentUser().getBalance())
        );
    }

    @Override
    public ResponseEntity<Void> makeAdmin(Integer id) {
        if (!userService.isCurrentUserAdmin()) {
            throw new NotAdminException();
        }
        userService.changeRole(userService.getById(id), Role.ADMIN);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> revokeAdmin(Integer id) {
        if (!userService.isCurrentUserAdmin()) {
            throw new NotAdminException();
        }
        userService.changeRole(userService.getById(id), Role.USER);
        return ResponseEntity.ok().build();
    }

    private UserInfoResponseTo transform(User user) {
        return new UserInfoResponseTo()
                .id(user.getId())
                .login(user.getUsername())
                .balance(user.getBalance());
    }
}
