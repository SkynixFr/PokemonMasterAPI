package org.example.pokemonmasterapi.classes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Salutations {
    private final String message;
    private final String lang;
}
