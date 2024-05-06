package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatChangeEntity {
    @PositiveOrZero
    private int change;

    private String stat;
}
