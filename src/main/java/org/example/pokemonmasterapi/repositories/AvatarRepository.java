package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.AvatarEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvatarRepository extends MongoRepository<AvatarEntity, String> {
    boolean existsByName(String name);
}
