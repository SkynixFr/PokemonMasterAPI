package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreate {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[\\w -]+$")
    private String name;

    @NotEmpty
    private String avatarId;
}
