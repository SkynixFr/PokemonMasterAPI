package org.example.pokemonmasterapi.repositories.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "moves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty
    private String name;

    @PositiveOrZero
    private int power;

    @PositiveOrZero
    private int accuracy;

    @Positive
    private int pp;

    @Valid
    private MetaEntity meta;

    @NotEmpty
    private String type;

    @NotEmpty
    private String category;

    @NotEmpty
    private String description;

    @NotEmpty
    private List<String> learnedBy;

    @Valid
    private List<StatChangeEntity> statsChange = null;

    @Valid
    private String target;
}
