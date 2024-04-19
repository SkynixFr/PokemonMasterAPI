package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.MoveCreate;
import org.example.pokemonmasterapi.repositories.MoveRepository;
import org.example.pokemonmasterapi.repositories.model.MoveEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/moves")
@CrossOrigin(origins = "http://localhost:3000")
public class MoveController {
    private final MoveRepository moveRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<MoveEntity> createMove(@RequestBody @Validated List<MoveCreate> moves) {
        moveRepository.deleteAll();
        List<MoveEntity> createdMoves = new ArrayList<>();
        moves.stream().map(move -> new MoveEntity(
                null,
                move.getName(),
                move.getPower(),
                move.getAccuracy(),
                move.getPp(),
                move.getMeta(),
                move.getType(),
                move.getCategory(),
                move.getDescription(),
                move.getLearnedBy()
        )).forEach(move -> createdMoves.add(moveRepository.save(move)));
        return createdMoves;
    }

    @GetMapping
    public List<MoveEntity> getMoves() {
        return moveRepository.findAll();
    }
}
