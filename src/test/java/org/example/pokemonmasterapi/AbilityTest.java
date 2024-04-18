package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.AbilityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AbilityTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AbilityRepository abilityRepository;

    @AfterEach
    public void tearDown() {
        abilityRepository.deleteAll();
    }

    @Test
    public void addAbilityReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/abilities")
                .content(
                        "{\"name\": \"Overgrow\",\"description\": \"Powers up Grass-type moves in a pinch\",\"learnedBy\": [\"Bulbasaur\",\"Ivysaur\",\"Venusaur\"]}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    public void getAbilitiesReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/abilities")
                .content(
                        "{\"name\": \"Overgrow\",\"description\": \"Powers up Grass-type moves in a pinch\",\"learnedBy\": [\"Bulbasaur\",\"Ivysaur\",\"Venusaur\"]}")
                .contentType(MediaType.APPLICATION_JSON));
        // When
        var response = mockMvc.perform(get("/abilities"));

        // Then
        response.andExpect(status().isOk());
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Overgrow");
    }

}
