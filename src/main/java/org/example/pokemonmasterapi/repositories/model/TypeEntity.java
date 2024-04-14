package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeEntity {
    @NotEmpty
    private String name;

    @NotNull
    @Valid
    private DamageRelation damageRelation;
}
