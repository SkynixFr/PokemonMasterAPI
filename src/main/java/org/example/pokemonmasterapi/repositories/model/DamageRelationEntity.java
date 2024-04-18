package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DamageRelationEntity {
    @NotEmpty
    private List<String> doubleDamageFrom;

    @NotEmpty
    private List<String> doubleDamageTo;

    @NotEmpty
    private List<String> halfDamageFrom;

    @NotEmpty
    private List<String> halfDamageTo;

    @NotEmpty
    private List<String> noDamageFrom;

    @NotEmpty
    private List<String> noDamageTo;
}
