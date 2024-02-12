package ru.ifmo.pokebet.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.service.AuthenticationService;
import ru.pokebet.openapi.api.AuthApi;
import ru.pokebet.openapi.model.JwtResponseTo;
import ru.pokebet.openapi.model.SignInRequestTo;
import ru.pokebet.openapi.model.SignUpRequestTo;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<JwtResponseTo> signIn(SignInRequestTo signInRequestTo) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequestTo));
    }

    @Override
    public ResponseEntity<JwtResponseTo> signUp(SignUpRequestTo signUpRequestTo) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequestTo));
    }
}
