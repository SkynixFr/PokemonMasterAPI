package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.NatureCreate;
import org.example.pokemonmasterapi.repositories.NatureRepository;
import org.example.pokemonmasterapi.repositories.model.NatureEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/natures")
@CrossOrigin(origins = "http://localhost:*")
public class NatureController {
    private final NatureRepository natureRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<NatureEntity> createNature(@RequestBody @Validated List<NatureCreate> natures) {
        natureRepository.deleteAll();
        List<NatureEntity> createdNatures = new ArrayList<>();
        natures.stream()
                .map(nature -> new NatureEntity(
                        null,
                        nature.getName(),
                        nature.getIncreasedStat(),
                        nature.getDecreasedStat()
                ))
                .forEach(nature -> createdNatures.add(natureRepository.save(nature)));
        return createdNatures;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NatureEntity> getNatures() {
        return natureRepository.findAll();
    }
}
