package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.MoveCreate;
import org.example.pokemonmasterapi.repositories.MoveRepository;
import org.example.pokemonmasterapi.repositories.model.MoveEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/moves")
@CrossOrigin(origins = "http://localhost:3000")
public class MoveController {
    private final MoveRepository moveRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addMove(@RequestBody @Validated MoveCreate move) {
        moveRepository.save(
                new MoveEntity(null, move.getName(), move.getPower(), move.getAccuracy(), move.getPp(),
                        move.getMeta(), move.getType(), move.getCategory(), move.getDescription(),
                        move.getLearnedBy()));
    }

    @GetMapping
    public List<MoveEntity> getMoves() {
        return moveRepository.findAll();
    }
}
