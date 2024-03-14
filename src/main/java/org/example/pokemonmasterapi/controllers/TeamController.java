package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Team;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamRepository teamRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team addTeam(@RequestBody Team team) {
        return teamRepository.save(team);
    }
}
