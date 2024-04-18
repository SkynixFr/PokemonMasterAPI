package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.AbilityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AbilityRepository extends MongoRepository<AbilityEntity, String> {
}
