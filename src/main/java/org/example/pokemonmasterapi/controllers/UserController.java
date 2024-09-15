package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.config.JWTService;
import org.example.pokemonmasterapi.controllers.model.*;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
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
    private final AvatarRepository avatarRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTService jwtService;

    public UserResponse mappingUserResponse(UserEntity user) {
        var avatar = avatarRepository.findById(user.getAvatarId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Avatar does not exist"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),avatar, user.getPokemonTeamIds(), user.getRole());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Validated UserCreate userCreate) {


        if (userRepository.existsByEmail(userCreate.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + userCreate.getEmail() + " already exists");

        }
        var avatar = avatarRepository.findById(userCreate.getAvatarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar does not exist"));
        var userBDD = userRepository.save(new UserEntity(null, userCreate.getUsername(), userCreate.getEmail(),
                passwordEncoder.encode(userCreate.getPassword()),avatar.getId(), null,"USER"));
        return new UserResponse(userBDD.getId(), userBDD.getUsername(), userBDD.getEmail(),avatar, userBDD.getPokemonTeamIds(), userBDD.getRole());
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

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse me(@RequestHeader("Authorization") String authorization) {
        var token = authorization.substring(7);
        var id = jwtService.extractId(token).toString();
        var userBDD = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return this.mappingUserResponse(userBDD);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@PathVariable String id, @RequestBody @Validated UserUpdate userUpdate) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with id " + id + " does not exist");
        }
        var userBDD = userRepository.findById(id).get();
        if (userUpdate.getEmail() == null && userUpdate.getPassword() == null && userUpdate.getUsername() == null && userUpdate.getAvatarId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must provide at least one field to update");
        }
        if (userRepository.existsByEmail(userUpdate.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + userUpdate.getEmail() + " already exists");
        }
        if(userUpdate.getUsername() != null)
        {
            userBDD.setUsername(userUpdate.getUsername());
        }
        if (userUpdate.getEmail() != null)
        {
            userBDD.setEmail(userUpdate.getEmail());
        }
        if (userUpdate.getPassword() != null)
        {
        userBDD.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }
        if (userUpdate.getAvatarId() != null)
        {
            userBDD.setAvatarId(userUpdate.getAvatarId());
        }
        userRepository.save(userBDD);
        return this.mappingUserResponse(userBDD);
    }

    @PostMapping("/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(@RequestHeader("Authorization") String authorization) {
        var token = authorization.substring(7);
        var id = jwtService.extractId(token).toString();
        var userBDD = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var accessToken = jwtService.generateToken(userBDD);
        return new RefreshTokenResponse(accessToken);
    }


}
