package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatEntity {
    @NotEmpty
    private String name;

    @PositiveOrZero
    private double value;

    @Positive
    private int max;

    @PositiveOrZero
    @Min(0)
    @Max(252)
    private int ev;

    @PositiveOrZero
    @Min(0)
    @Max(31)
    private int iv;

    @PositiveOrZero
    @Min(0)
    private int total;

    @PositiveOrZero
    @Min(0)
    private int base;
}
