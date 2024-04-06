package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.model.Avatar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvatarRepository extends MongoRepository<Avatar, String>{
    List<Avatar> findByName(String name);
}
