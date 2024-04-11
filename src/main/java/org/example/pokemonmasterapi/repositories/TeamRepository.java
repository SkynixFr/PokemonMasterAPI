package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.repositories.model.TeamEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<TeamEntity, String> {
    boolean existsByName(String teamName);
}
