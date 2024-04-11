package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonEntity {
    @NotEmpty
    private String name;

    @NotEmpty
    @Valid
    private TypeEntity[] types;

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
    private MoveEntity[] moves;
    
    @Valid
    private ItemEntity item;

    @NotEmpty
    @Valid
    private StatEntity[] stats;

}
