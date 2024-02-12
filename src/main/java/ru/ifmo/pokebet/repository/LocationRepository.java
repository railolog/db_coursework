package ru.ifmo.pokebet.repository;

import java.util.List;
import java.util.Optional;

import ru.ifmo.pokebet.domain.Location;

public interface LocationRepository {
    List<Location> findAll();
    Optional<Location> findById(int id);
}
