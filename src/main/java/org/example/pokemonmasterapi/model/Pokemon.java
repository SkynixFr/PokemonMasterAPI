package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private String name;
    private Type[] type;
    private int level;
    private Ability ability;
    private String nature;
    private String gender = "N";
    private boolean isShiny;
    private int id;
    private Move[] moves;
    private Item item;
    private Stats stats;
    private Sprite sprites;
}
