package org.example.pokemonmasterapi.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String image;
}
