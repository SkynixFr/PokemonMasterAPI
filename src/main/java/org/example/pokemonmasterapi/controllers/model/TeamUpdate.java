package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pokemonmasterapi.repositories.model.PokemonEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdate {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[\\w -]+$")
    private String name;

    @NotEmpty
    private String avatarId;

    @Size(min = 1, max = 6)
    @Valid
    private List<PokemonEntity> pokemons;
}