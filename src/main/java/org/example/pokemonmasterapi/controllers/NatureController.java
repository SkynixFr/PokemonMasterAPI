package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.NatureCreate;
import org.example.pokemonmasterapi.repositories.NatureRepository;
import org.example.pokemonmasterapi.repositories.model.NatureEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/natures")
@CrossOrigin(origins = "http://localhost:3000")
public class NatureController {
    private final NatureRepository natureRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addNature(@RequestBody NatureCreate nature) {
        natureRepository.save(
                new NatureEntity(null, nature.getName(), nature.getIncreasedStat(), nature.getDecreasedStat()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NatureEntity> getNatures() {
        return natureRepository.findAll();
    }
}
