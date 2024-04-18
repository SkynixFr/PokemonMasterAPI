package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.AbilityCreate;
import org.example.pokemonmasterapi.repositories.AbilityRepository;
import org.example.pokemonmasterapi.repositories.model.AbilityEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/abilities")
@CrossOrigin(origins = "http://localhost:3000")
public class AbilityController {
    private final AbilityRepository abilityRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAbility(@RequestBody @Validated AbilityCreate ability) {
        abilityRepository.save(
                new AbilityEntity(null, ability.getName(), ability.getDescription(), ability.getLearnedBy()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AbilityEntity> getAbilities() {
        return abilityRepository.findAll();
    }
}
