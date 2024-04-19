package org.example.pokemonmasterapi.controllers.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    private String email;
    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*\\W)[A-Za-z\\d\\W]{8,}$")
    private String password;
}
