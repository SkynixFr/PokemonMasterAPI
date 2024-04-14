package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonEntity {
    @NotEmpty
    private String name;

    @NotEmpty
    @Valid
    private List<TypeEntity> types;

    @Min(1)
    private int level;

    @NotNull
    @Valid
    private AbilityEntity ability;

    @NotEmpty
    private String nature;

    @NotNull
    private GenderEnum gender = GenderEnum.Neutral;

    private boolean isShiny = false;

    @Positive
    private int id;

    @NotEmpty
    @Size(min = 1, max = 4)
    @Valid
    private List<MoveEntity> moves;

    @Valid
    private ItemEntity item;

    @NotEmpty
    @Size(min = 6)
    @Valid
    private List<StatEntity> stats;

    @Positive
    private int weight;
}
