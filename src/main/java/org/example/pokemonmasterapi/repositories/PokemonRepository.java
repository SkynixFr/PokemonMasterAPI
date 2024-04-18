package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.PokemonEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokemonRepository extends MongoRepository<PokemonEntity, String> {
}
