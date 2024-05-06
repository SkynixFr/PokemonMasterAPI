package org.example.pokemonmasterapi.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pokemonmasterapi.repositories.model.AvatarEntity;
import org.example.pokemonmasterapi.repositories.model.PokemonTeamEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private String id;

    private String name;

    private AvatarEntity avatar;

    private List<PokemonTeamEntity> pokemons;
}