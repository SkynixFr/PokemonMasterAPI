package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.model.User;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    public void registerUserReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\",\"email\": \"IlovePikachu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isCreated());
        response.andExpect(content().string("User created"));

    }

    @Test
    public void registerUserReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Missing required data. Please provide email, username, and password"));
    }

    @Test
    public void registerUserReturnConflictStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword("Pikachu");
        user.setEmail("ILovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\",\"email\": \"IlovePichu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Username already exists"));
    }

    @Test
    public void registerUserReturnConflictStatus2() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword("Pikachu");
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky2\",\"password\": \"Pikachu\",\"email\": \"IlovePikachu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Email already exists"));
    }
}
