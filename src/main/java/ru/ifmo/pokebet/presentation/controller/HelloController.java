package ru.ifmo.pokebet.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.auth.model.User;
import ru.ifmo.pokebet.auth.service.UserService;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final UserService userService;

    @PostMapping("/hello")
    public ResponseEntity<String> hello(@RequestBody String body) {
        return ResponseEntity.ok(body);
    }

    @Secured("USER")
    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(user.getUsername());
    }
}
