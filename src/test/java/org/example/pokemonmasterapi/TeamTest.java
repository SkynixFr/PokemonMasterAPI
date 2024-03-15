package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.TeamRepository;
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
class TeamTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    public void addTeamReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Rocket\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.name").value("Team Rocket"));
    }

    @Test
    public void addTeamReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.message").value("Name is required"));
    }

    @Test
    public void addTeamReturnConflictStatus() throws Exception {
        // Given
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Rocket\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Rocket\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(jsonPath("$.message").value("Team already exists"));

    }
}
