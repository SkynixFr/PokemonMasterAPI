package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String>{

}
