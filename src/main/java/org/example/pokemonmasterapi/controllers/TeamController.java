package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Team;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamRepository teamRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team addTeam(@RequestBody Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return teamRepository.save(team);
    }
}
