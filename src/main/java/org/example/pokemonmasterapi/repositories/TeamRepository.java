package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends MongoRepository<Team, UUID>{
}
