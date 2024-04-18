package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.NatureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NatureRepository extends MongoRepository<NatureEntity, String> {
}
