package org.example.pokemonmasterapi.repositories.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[\\w -]+$")
    private String username;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    private String email;

    @NotBlank
    private String password;

    @Size(min = 1, max = 6)
    @Valid
    private List<String> pokemonTeamIds;

    @NotBlank
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN)$")
    private String Role;

}
