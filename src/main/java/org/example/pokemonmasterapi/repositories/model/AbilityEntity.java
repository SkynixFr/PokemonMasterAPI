package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbilityEntity {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
