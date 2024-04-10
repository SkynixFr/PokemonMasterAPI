package org.example.pokemonmasterapi.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    @NotEmpty
    private String name;

    @NotEmpty
    @Valid
    private Type[] types;

    @Min(1)
    private int level;

    @NotEmpty
    @Valid
    private Ability ability;

    @NotEmpty
    private String nature;

    @NotEmpty
    @Size(min = 1, max = 1)
    private String gender = "N";

    private boolean isShiny = false;

    @NotEmpty
    private int id;

    @NotEmpty
    @Valid
    private Move[] moves;

    @NotEmpty
    @Valid
    private Item item;

    @NotEmpty
    @Valid
    private Stat[] stats;

}
