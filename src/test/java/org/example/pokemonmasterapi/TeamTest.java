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
import static org.assertj.core.api.Assertions.*;

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
        response.andExpect(content().string("Team created"));
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
        response.andExpect(content().string("Name is required"));
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
        response.andExpect(content().string("Team already exists"));

    }

    @Test
    public void returnAllTeamsOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Rocket\"}")
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Aqua\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/teams"));

        // Then
        response.andExpect(status().isOk());
        assertThat(teamRepository.findAll()).hasSize(2);
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Team Rocket", "Team Aqua");
    }

    @Test
    public void returnOneTeamOkStatus() throws  Exception {
        // Given
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Rocket\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/teams/Team Rocket"));

        // Then
        response.andExpect(status().isOk());
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Team Rocket");
    }

    @Test
    public void returnOneTeamNotFoundStatus() throws  Exception {
        // Given

        // When
        var response = mockMvc.perform(get("/teams/Team Rocket"));

        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("Team not found"));
    }
}
