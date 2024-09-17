package org.example.pokemonmasterapi.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pokemonmasterapi.repositories.model.AvatarEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;

    private String username;

    private String email;
    private String password;
    private AvatarEntity avatar;
    private List<String> pokemonTeamIds;
    private String role;
}
