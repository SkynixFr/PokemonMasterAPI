package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Team;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    private final TeamRepository teamRepository;

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestBody Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        }

        if (!teamRepository.findByName(team.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Team already exists");
        }

        teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body("Team created");
    }

    @GetMapping
    public ResponseEntity<Object> getTeams() {
        return ResponseEntity.status(HttpStatus.OK).body(teamRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Object> getTeam(@PathVariable String name) {
        var team = teamRepository.findByName(name);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(team.get(0));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Object> deleteTeam(@PathVariable String name) {
        var team = teamRepository.findByName(name);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
        teamRepository.delete(team.get(0));
        return ResponseEntity.status(HttpStatus.OK).body("Team deleted");
    }
}