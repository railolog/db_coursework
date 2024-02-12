package ru.ifmo.pokebet.presentation.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pokebet.domain.Location;
import ru.ifmo.pokebet.service.LocationQueryService;
import ru.pokebet.openapi.api.LocationApi;
import ru.pokebet.openapi.model.LocationListTo;
import ru.pokebet.openapi.model.LocationTo;

@RestController
@RequiredArgsConstructor
public class LocationController implements LocationApi {
    private final LocationQueryService locationQueryService;
    private final MainTransformer mainTransformer;

    public ResponseEntity<LocationListTo> getAllLocations() {
        return ResponseEntity.ok(mainTransformer.transformLocationList(locationQueryService.getAll()));
    }

    public ResponseEntity<LocationTo> getLocation(Integer id) {
        Optional<Location> byIdOpt = locationQueryService.getById(id);

        return byIdOpt
                .map(mainTransformer::transform)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
