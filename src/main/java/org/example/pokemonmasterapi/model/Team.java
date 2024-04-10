package org.example.pokemonmasterapi.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String name;

    @NotEmpty
    @Valid
    private Avatar avatar;

    @Size(min = 1, max = 6)
    @Valid
    private List<Pokemon> pokemons;
}
