package org.example.pokemonmasterapi.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Move {
    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    @NotEmpty
    private String category;

    @Positive
    private int power;

    @Positive
    private int accuracy;

    @Positive
    private int pp;

    @NotEmpty
    private String description;
}
