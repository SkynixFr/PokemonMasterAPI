package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbilityCreate {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private List<String> learnedBy;
}
