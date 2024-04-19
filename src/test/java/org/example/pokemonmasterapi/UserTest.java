package org.example.pokemonmasterapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pokemonmasterapi.controllers.model.UserResponse;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.example.pokemonmasterapi.repositories.model.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
        avatarRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void RegisterUserReturnCreatedStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        var responseID = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/id").asText();
        response.andExpect(status().isCreated());
        response.andExpect(content().json(objectMapper.writeValueAsString(new UserResponse(responseID, "Ricky","IlovePikachu@gmail.com", null, "USER"))));
    }

    @Test
    public void RegisterUserReturnConflictStatus() throws Exception {
        // Given
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", "JesuisunMDP-33", null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(status().reason("User with email IlovePikachu@gmail.com already exists"));
    }

    @Test
    public void RegisterUserWithEmailInvalidReturnBadRequestStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUserWithRoleInvalidReturnBadRequestStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content(
                                "{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmaiL.com\",\"role\": \"USER2\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUserWithUsernameInvalidReturnBadRequestStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content(
                                "{\"username\": \"\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
         }



}
