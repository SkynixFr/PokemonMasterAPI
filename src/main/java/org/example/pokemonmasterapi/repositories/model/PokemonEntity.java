package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "pokemons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonEntity {
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
    private List<MoveEntity> moves = new ArrayList<>();

    @Valid
    private ItemEntity item;

    @Size(min = 6)
    @Valid
    private List<StatEntity> stats;

    @Positive
    private int weight;
}
