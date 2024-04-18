package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.PokemonCreate;
import org.example.pokemonmasterapi.repositories.PokemonRepository;
import org.example.pokemonmasterapi.repositories.model.PokemonEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/pokemons")
@CrossOrigin(origins = "http://localhost:3000")
public class PokemonController {
    private final PokemonRepository pokemonRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPokemon(@RequestBody @Validated PokemonCreate pokemon) {
        pokemonRepository.save(
                new PokemonEntity(null, pokemon.getPokedexId(), pokemon.getName(), pokemon.getTypes(), 1, null,
                        null, pokemon.getGender(), false, null, null, pokemon.getStats(), pokemon.getWeight()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PokemonEntity> getPokemons() {
        return pokemonRepository.findAll();
    }
}
