package org.example.pokemonmasterapi.repositories.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaEntity {
    @NotEmpty
    private String ailment;

    @PositiveOrZero
    private int drain;

    @PositiveOrZero
    private int healing;

    @PositiveOrZero
    private int critRate;

    @PositiveOrZero
    private int priority;

    @PositiveOrZero
    private int effectChance;

    @PositiveOrZero
    private int flinchChance;

    @PositiveOrZero
    private int statChance;

    @PositiveOrZero
    private int minHits;

    @PositiveOrZero
    private int maxHits;

    @PositiveOrZero
    private int minTurns;

    @PositiveOrZero
    private int maxTurns;
}
