package org.example.pokemonmasterapi.repositories.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "abilities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private List<String> learnedBy;
}
