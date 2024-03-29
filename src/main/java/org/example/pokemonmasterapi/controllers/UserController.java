package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.example.pokemonmasterapi.config.JWTService;
import org.example.pokemonmasterapi.model.User;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required data. Please provide email, username, and password");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Hash the password before saving it to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved=userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }



    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User userRequest) {
        if (userRequest.getUsername() == null || userRequest.getPassword() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }
        if (userRepository.findByUsername(userRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Check if the password is correct
        if (!new BCryptPasswordEncoder().matches(userRequest.getPassword(), userRepository.findByUsername(userRequest.getUsername()).get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect");
        }


        // Generate a token for the user
        User userFound = userRepository.findByUsername(userRequest.getUsername()).get();
        return ResponseEntity.status(HttpStatus.OK).body("accessToken : " + "\"" + jwtService.generateToken(userFound) + "\"");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing id");
        }
        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
    @DeleteMapping("/")
    public ResponseEntity<Object> deleteWithoutId() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing id");
    }
    @PutMapping("/")
    public ResponseEntity<Object> putWithoutId() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing id");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String username = jwtAuthenticationToken.getToken().getClaim("username");
        if(userRepository.findByUsername(username).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findByUsername(username).get());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id,@RequestBody User user) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required data. Please provide email or username or password");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Hash the password before saving it to the database

        User userFound = userRepository.findById(id).get();
        if (user.getUsername() != null){
            userFound.setUsername(user.getUsername());
        }
        if (user.getPassword() != null){
            userFound.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getEmail() != null){
            userFound.setEmail(user.getEmail());
        }
        userFound = userRepository.save(userFound);
        return ResponseEntity.status(HttpStatus.OK).body(userFound);
    }



}
