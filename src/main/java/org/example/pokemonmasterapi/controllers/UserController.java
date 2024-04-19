package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.UserCreate;
import org.example.pokemonmasterapi.controllers.model.UserResponse;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.example.pokemonmasterapi.repositories.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Validated UserCreate userCreate) {

        if (userRepository.existsByEmail(userCreate.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + userCreate.getEmail() + " already exists");
        }
        var userBDD = userRepository.save(new UserEntity(null, userCreate.getUsername(), userCreate.getEmail(),
                userCreate.getPassword(), null,"USER"));
        return new UserResponse(userBDD.getId(), userBDD.getUsername(), userBDD.getEmail(), userBDD.getPokemonTeamIds(), userBDD.getRole());
    }
}
