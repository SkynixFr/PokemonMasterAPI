package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Salutations {
    private final String message;
    private final String lang;
}
