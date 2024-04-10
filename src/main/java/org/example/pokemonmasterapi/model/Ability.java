package org.example.pokemonmasterapi.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ability {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
