package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private String name;
    private String type;
    private String type2;
    private int level;
    private String description;
    private Ability ability;
    private String nature;
    private String sex;
    private boolean isShiny;
    private int pokedexNumber;
    private Move[] moves;
    private Item Item;
    private Stats stats;
    private int IV;
    private int EV;
}
