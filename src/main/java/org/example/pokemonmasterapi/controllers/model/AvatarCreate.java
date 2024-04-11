package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarCreate {
    @NotEmpty
    @Pattern(regexp = "^[\\w -]+$")
    private String name;

    @NotEmpty
    private String region;

    @NotEmpty
    private String url;
}
