package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
public class Move {
    private String name;
    private String type;
    private String category;
    private int power;
    private int accuracy;
    private int pp;
    private String description;
}
