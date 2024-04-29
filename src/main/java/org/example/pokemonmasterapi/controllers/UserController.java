package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.config.JWTService;
import org.example.pokemonmasterapi.controllers.model.TokenResponse;
import org.example.pokemonmasterapi.controllers.model.UserCreate;
import org.example.pokemonmasterapi.controllers.model.UserLogin;
import org.example.pokemonmasterapi.controllers.model.UserResponse;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.example.pokemonmasterapi.repositories.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Validated UserCreate userCreate) {


        if (userRepository.existsByEmail(userCreate.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + userCreate.getEmail() + " already exists");

        }
        var userBDD = userRepository.save(new UserEntity(null, userCreate.getUsername(), userCreate.getEmail(),
                passwordEncoder.encode(userCreate.getPassword()), null,"USER"));
        return new UserResponse(userBDD.getId(), userBDD.getUsername(), userBDD.getEmail(), userBDD.getPokemonTeamIds(), userBDD.getRole());
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login (@RequestBody @Validated UserLogin userLogin){
        if (!userRepository.existsByEmail(userLogin.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with email " + userLogin.getEmail() + " does not exist");
        }
        var userBDD = userRepository.findByEmail(userLogin.getEmail());
        if (!passwordEncoder.matches(userLogin.getPassword(), userBDD.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password for user with email " + userLogin.getEmail() + " is incorrect");
        }
        var accessToken = jwtService.generateToken(userBDD);
        var refreshToken = jwtService.generateRefreshToken(userBDD);
        return new TokenResponse(accessToken, refreshToken);
    }


}
