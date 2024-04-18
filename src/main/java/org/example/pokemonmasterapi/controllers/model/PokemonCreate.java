package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pokemonmasterapi.repositories.model.GenderEnum;
import org.example.pokemonmasterapi.repositories.model.StatEntity;
import org.example.pokemonmasterapi.repositories.model.TypeEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonCreate {
    @Positive
    private int pokedexId;

    @NotEmpty
    private String name;

    @NotNull
    @Valid
    private List<TypeEntity> types;

    @NotNull
    private GenderEnum gender;

    @Size(min = 6)
    @Valid
    private List<StatEntity> stats;

    @Positive
    private int weight;
}
