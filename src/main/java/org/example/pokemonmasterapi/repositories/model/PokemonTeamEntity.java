package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonTeamEntity {
    @Id
    @Positive
    private int pokedexId;

    @NotEmpty
    private String name;

    @NotNull
    @Valid
    private List<TypeEntity> types;

    @Min(1)
    private int level;

    @Valid
    private AbilityEntity ability;

    @Valid
    private NatureEntity nature;

    @NotNull
    private GenderEnum gender = GenderEnum.neutral;

    private boolean isShiny = false;

    @Size(min = 1, max = 4)
    @Valid
    private List<MoveEntity> moves;

    @Valid
    private ItemEntity item;

    @Size(min = 6, max = 6)
    @Valid
    private List<StatEntity> stats;

    @Positive
    private int weight;
}
