package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pokemonmasterapi.repositories.model.MetaEntity;
import org.example.pokemonmasterapi.repositories.model.StatChangeEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveCreate {
    @NotEmpty
    private String name;

    @PositiveOrZero
    private int power;

    @PositiveOrZero
    private int accuracy;

    @Positive
    private int pp;

    @Positive
    private int maxPp;

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
