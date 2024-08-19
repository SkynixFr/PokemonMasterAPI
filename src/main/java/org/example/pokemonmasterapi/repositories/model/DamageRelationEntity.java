package org.example.pokemonmasterapi.repositories.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DamageRelationEntity {

    private List<String> doubleDamageFrom;


    private List<String> doubleDamageTo;


    private List<String> halfDamageFrom;


    private List<String> halfDamageTo;


    private List<String> noDamageFrom;


    private List<String> noDamageTo;
}
