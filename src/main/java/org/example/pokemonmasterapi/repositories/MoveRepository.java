package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.MoveEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MoveRepository extends MongoRepository<MoveEntity, String> {
}
