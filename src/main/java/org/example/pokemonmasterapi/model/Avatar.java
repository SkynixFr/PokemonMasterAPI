package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    private String name;
    private String location;
    private String url;
}
