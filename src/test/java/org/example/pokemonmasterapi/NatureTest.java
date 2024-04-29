package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.NatureRepository;
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
public class NatureTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NatureRepository natureRepository;

    @AfterEach
    public void tearDown() {
        natureRepository.deleteAll();
    }

    @Test
    public void addNatureReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/natures")
                .content("[{" +
                        "  \"name\": \"Adamant\"," +
                        "  \"increasedStat\": \"Attack\"," +
                        "  \"decreasedStat\": \"Special Attack\"" +
                        "}]")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    public void getNaturesReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/natures")
                .content("[{" +
                        "  \"name\": \"Adamant\"," +
                        "  \"increasedStat\": \"Attack\"," +
                        "  \"decreasedStat\": \"Special Attack\"" +
                        "}]")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/natures"));

        // Then
        response.andExpect(status().isOk());
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Adamant");
    }
}
