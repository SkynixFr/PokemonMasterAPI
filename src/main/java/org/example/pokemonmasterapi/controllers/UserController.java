package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.config.JWTService;
import org.example.pokemonmasterapi.model.User;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
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
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }



    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User userRequest) {
        if (userRequest.getUsername() == null || userRequest.getPassword() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }
        if (userRepository.findByUsername(userRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!new BCryptPasswordEncoder().matches(userRequest.getPassword(), userRepository.findByUsername(userRequest.getUsername()).get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect");
        }
        User userFound = userRepository.findByUsername(userRequest.getUsername()).get();

        return ResponseEntity.status(HttpStatus.OK).body("Access Token : " + "\"" + jwtService.generateToken(userFound) + "\"");
    }


}