package org.example.pokemonmasterapi.repositories.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatChangeEntity {
    private int change;

    private String stat;
}
