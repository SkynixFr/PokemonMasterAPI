package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Team;
import org.example.pokemonmasterapi.model.Pokemon;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    private final TeamRepository teamRepository;

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestBody Team team) {
        if (team.getName() == null || team.getAvatar() == null || team.getName().isEmpty() || team.getAvatar().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing name or avatar");
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

    @PostMapping("/{name}/pokemons/{pokemonName}")
    public ResponseEntity<Object> addPokemon(@PathVariable String name, @PathVariable String pokemonName, @RequestBody Pokemon pokemonData) {
        var team = teamRepository.findByName(name);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }

        var pokemons = team.get(0).getPokemons();
        if (pokemons.stream().anyMatch(pokemon -> pokemon.getName().equals(pokemonName))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokemon already exists");
        }
        var Pokemon = new Pokemon(pokemonData.getName(),pokemonData.getType(),pokemonData.getType2(),pokemonData.getLevel(),pokemonData.getDescription(),pokemonData.getAbility(),pokemonData.getNature(),pokemonData.getSex(),pokemonData.isShiny(),
                pokemonData.getPokedexNumber(),pokemonData.getMoves(),pokemonData.getItem(),pokemonData.getStats(),pokemonData.getIV(),pokemonData.getEV());
        pokemons.add(Pokemon);
        teamRepository.save(team.get(0));
        return ResponseEntity.status(HttpStatus.CREATED).body("Pokemon added");
    }
}