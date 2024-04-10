package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Team;
import org.example.pokemonmasterapi.model.Pokemon;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    private final TeamRepository teamRepository;

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestBody @Validated Team team) {
        if (team.getName() == null || team.getAvatar() == null || team.getName().isEmpty() || team.getAvatar().getName().isEmpty() || team.getAvatar().getUrl().isEmpty() || team.getAvatar().getLocation().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing name or avatar");
        }

        if (!) {
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

    @PutMapping("/{id}/pokemons/")
    public ResponseEntity<Object> saveTeam(@PathVariable String id, @RequestBody Team team) {
        var teamBdd = teamRepository.findByName(name);
        if (teamBdd.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }

        if (teamBdd.get(0).getPokemons().size() + team.getPokemons().size() > 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Team can't have more than 6 pokemons");
        }

        teamRepository.save(team);

        return ResponseEntity.status(HttpStatus.CREATED).body("Team saved");
    }
}