package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.repositories.AbilityRepository;
import org.example.pokemonmasterapi.repositories.model.AbilityEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/abilities")
@CrossOrigin(origins = "http://localhost:3000")
public class AbilityController {
    private final AbilityRepository abilityRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<AbilityEntity> createAbility(@RequestBody @Validated List<AbilityEntity> abilities) {
        abilityRepository.deleteAll();
        List<AbilityEntity> createdAbilities = new ArrayList<>();
        abilities.stream().map(ability -> new AbilityEntity(
                null,
                ability.getName(),
                ability.getDescription(),
                ability.getLearnedBy()
        )).forEach(ability -> createdAbilities.add(abilityRepository.save(ability)));
        return createdAbilities;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AbilityEntity> getAbilities() {
        return abilityRepository.findAll();
    }
}
