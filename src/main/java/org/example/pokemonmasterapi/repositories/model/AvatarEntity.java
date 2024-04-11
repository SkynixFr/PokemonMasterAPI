package org.example.pokemonmasterapi.repositories.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String region;

    @NotEmpty
    private String url;
}
