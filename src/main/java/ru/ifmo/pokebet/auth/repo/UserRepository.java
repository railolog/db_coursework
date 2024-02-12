package ru.ifmo.pokebet.auth.repo;

import java.util.Optional;

import ru.ifmo.pokebet.auth.model.User;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);

    User update(User user);
}
