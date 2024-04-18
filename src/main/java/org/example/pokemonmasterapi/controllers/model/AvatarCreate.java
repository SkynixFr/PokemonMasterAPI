package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarCreate {
    @NotEmpty
    private String name;

    @NotEmpty
    private String region;

    @NotEmpty
    private String sprite;
}
