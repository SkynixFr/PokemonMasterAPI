package org.example.pokemonmasterapi.config.auth;

import lombok.RequiredArgsConstructor;

import org.example.pokemonmasterapi.config.JwtService;
import org.example.pokemonmasterapi.model.Role;
import org.example.pokemonmasterapi.model.User;
import org.example.pokemonmasterapi.repositories.UserRepository;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
    var user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.User);
    userRepository.save(user);
    var jwtToken= jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken= jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
