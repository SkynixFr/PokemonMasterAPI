package org.example.pokemonmasterapi.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stat {
    @NotEmpty
    private String name;

    @Positive
    private int value;

    @Positive
    private int max;
}
