package org.example.pokemonmasterapi.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    @NotEmpty
    private String name;

    @NotEmpty
    private String location;

    @NotEmpty
    private String url;
}
