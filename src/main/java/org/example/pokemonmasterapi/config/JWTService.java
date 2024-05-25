package org.example.pokemonmasterapi.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.example.pokemonmasterapi.repositories.UserRepository;
import org.example.pokemonmasterapi.repositories.model.UserEntity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service

public class JWTService {

    private final JwtEncoder jwtEncoder;
    private UserRepository userRepository;
    private final JwtDecoder jwtDecoder;
    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(UserEntity user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                // Token expires in 1 hour
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("id", user.getId())
                .claim("password", user.getPassword())
                .claim("role", user.getRole())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public String generateRefreshToken(UserEntity user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                // Token expires in 1 day
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("id", user.getId())
                .claim("password", user.getPassword())
                .claim("role", user.getRole())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
    public Map<String, Object> decodeToken(String token) {
        return this.jwtDecoder.decode(token).getClaims();
    }
    public Object extractId(String token) {
        return this.decodeToken(token).get("id");
    }

}
