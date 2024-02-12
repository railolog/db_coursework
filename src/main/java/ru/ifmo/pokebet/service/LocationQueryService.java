package ru.ifmo.pokebet.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationQueryService {
    private final LocationRepository locationRepository;

    public Optional<Location> getById(int id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public boolean existsById(int id) {
        return locationRepository.findById(id).isPresent();
    }
}
