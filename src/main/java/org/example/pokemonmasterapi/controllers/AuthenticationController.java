package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.pokemonmasterapi.config.auth.AuthenticationResponse;
import org.example.pokemonmasterapi.config.auth.AuthenticationService;
import org.example.pokemonmasterapi.config.auth.LoginRequest;
import org.example.pokemonmasterapi.config.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
//@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}

