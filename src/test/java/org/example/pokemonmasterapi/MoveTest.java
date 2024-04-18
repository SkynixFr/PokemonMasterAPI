package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.MoveRepository;
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
public class MoveTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MoveRepository moveRepository;

    @AfterEach
    public void tearDown() {
        moveRepository.deleteAll();
    }

    @Test
    public void addMoveReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/moves")
                .content("{" +
                        "  \"name\": \"Tackle\"," +
                        "  \"power\": 40," +
                        "  \"accuracy\": 100," +
                        "  \"pp\": 35," +
                        "  \"meta\": {" +
                        "    \"ailment\": \"Physical\"," +
                        "    \"drain\": 0," +
                        "    \"healing\": 0," +
                        "    \"critRate\": 0," +
                        "    \"priority\": 0," +
                        "    \"effectChance\": 0," +
                        "    \"flinchChance\": 0," +
                        "    \"statChance\": 0," +
                        "    \"minHits\": 0," +
                        "    \"maxHits\": 0," +
                        "    \"minTurns\": 0," +
                        "    \"maxTurns\": 0" +
                        "  }," +
                        "  \"type\": \"Normal\"," +
                        "  \"category\": \"Physical\"," +
                        "  \"description\": \"A physical attack in which the user charges and slams into the target with its whole body.\"," +
                        "  \"learnedBy\": [\"Bulbasaur\", \"Charmander\", \"Squirtle\"]" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    public void getMovesReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/moves")
                .content("{" +
                        "  \"name\": \"Tackle\"," +
                        "  \"power\": 40," +
                        "  \"accuracy\": 100," +
                        "  \"pp\": 35," +
                        "  \"meta\": {" +
                        "    \"ailment\": \"Physical\"," +
                        "    \"drain\": 0," +
                        "    \"healing\": 0," +
                        "    \"critRate\": 0," +
                        "    \"priority\": 0," +
                        "    \"effectChance\": 0," +
                        "    \"flinchChance\": 0," +
                        "    \"statChance\": 0," +
                        "    \"minHits\": 0," +
                        "    \"maxHits\": 0," +
                        "    \"minTurns\": 0," +
                        "    \"maxTurns\": 0" +
                        "  }," +
                        "  \"type\": \"Normal\"," +
                        "  \"category\": \"Physical\"," +
                        "  \"description\": \"A physical attack in which the user charges and slams into the target with its whole body.\"," +
                        "  \"learnedBy\": [\"Bulbasaur\", \"Charmander\", \"Squirtle\"]" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/moves"));

        // Then
        response.andExpect(status().isOk());
        assertThat(moveRepository.findAll()).hasSize(1);
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Tackle");
    }
}
