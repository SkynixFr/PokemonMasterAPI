package org.example.pokemonmasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    private String name;
    private String location;
    private String url;
}
