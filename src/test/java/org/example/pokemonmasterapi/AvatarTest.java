package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.AvatarRepository;
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
public class AvatarTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AvatarRepository avatarRepository;

    @AfterEach
    public void tearDown() {
        avatarRepository.deleteAll();
    }

    @Test
    public void addAvatarReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/avatars")
                .content("{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
        response.andExpect(content().string("Avatar created"));
    }

    @Test
    public void addAvatarReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/avatars")
                .content("{\"name\": \"\",\"location\": \"\",\"url\": \"\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Missing name, location or url"));
    }

    @Test
    public void addAvatarReturnConflictStatus() throws Exception {
        // Given
        mockMvc.perform(post("/avatars")
                .content("{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(post("/avatars")
                .content("{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Avatar already exists"));
    }

    @Test
    public void getAvatarsReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/avatars")
                .content("{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/avatars"));

        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().json("[{\"name\":\"Red\",\"location\":\"Kanto\",\"url\":\"~/public/images/compressed/avatars/kanto/Ash.png\"}]"));
    }
}
