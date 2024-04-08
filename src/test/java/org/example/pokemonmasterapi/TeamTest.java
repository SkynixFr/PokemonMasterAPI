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
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
        response.andExpect(content().string("Team created"));
    }

    @Test
    public void addTeamReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var avatar = "{\"name\": \"\",\"location\": \"\",\"url\": \"\"}";
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));


        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Missing name or avatar"));
    }

    @Test
    public void addTeamReturnConflictStatus() throws Exception {
        // Given
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var avatar2 = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        var response = mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar2 + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Team already exists"));

    }

    @Test
    public void returnAllTeamsOkStatus() throws Exception {
        // Given
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));
        var avatar2 = "{\"name\": \"Cynthia\",\"location\": \"Sinnoh\",\"url\": \"~/public/images/compressed/avatars/sinnoh/cynthia.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Cynthia\",\"avatar\": " + avatar2 + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/teams"));

        // Then
        response.andExpect(status().isOk());
        assertThat(teamRepository.findAll()).hasSize(2);
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Team Red", "Team Cynthia");
    }

    @Test
    public void returnOneTeamOkStatus() throws  Exception {
        // Given
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/teams/Team Red"));

        // Then
        response.andExpect(status().isOk());
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Team Red");
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

    @Test
    public void deleteTeamOkStatus() throws  Exception {
        // Given
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(delete("/teams/Team Red"));

        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().string("Team deleted"));
    }

    @Test
    public void deleteTeamNotFoundStatus() throws  Exception {
        // Given

        // When
        var response = mockMvc.perform(delete("/teams/Team Red"));

        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("Team not found"));
    }

    @Test
    public void addPokemonReturnCreatedStatus() throws Exception {
        // Given
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));
        var moves = "[{\"name\": \"Thunderbolt\",\"type\": \"Electric\",\"category\": \"Special\",\"power\": 90,\"accuracy\": 100,\"pp\": 15,\"description\": \"A strong electric attack\"}]";
        var types = "[{\"name\": \"Electric\"}]";
        var item = "{\"name\": \"Light Ball\",\"description\": \"A strange ball that boosts Pikachu's stats\"}";
        var stats = "[{\"name\": \"hp\",\"value\": 35,\"max\": 35},{\"name\": \"Attack\",\"value\": 55,\"max\": 55},{\"name\": \"Defense\",\"value\": 40,\"max\": 40},{\"name\": \"Sp. Attack\",\"value\": 50,\"max\": 50},{\"name\": \"Sp. Defense\",\"value\": 50,\"max\": 50},{\"name\": \"Speed\",\"value\": 90,\"max\": 90}]";
        var ability = "{\"name\": \"Static\",\"description\": \"May cause paralysis if touched\"}";
        var pokemon = "{\"name\": \"Pikachu\",\"type\": " + types + ",\"level\": 5,\"gender\": \"Male\",\"isShiny\": false,\"id\": 25,\"ability\": " + ability + ",\"nature\": \"Brave\",\"moves\": " + moves + ",\"item\": " + item + ",\"stats\": " + stats + "}";
        // When
        var response = mockMvc.perform(post("/teams/Team Red/pokemons/Pikachu").contentType(MediaType.APPLICATION_JSON).content(pokemon));

        // Then
        response.andExpect(status().isCreated());
        response.andExpect(content().string("Pokemon added"));
    }

    @Test
    public void addPokemonReturnNotFoundStatus() throws Exception {
        // Given

        // When
        var moves = "[{\"name\": \"Thunderbolt\",\"type\": \"Electric\",\"category\": \"Special\",\"power\": 90,\"accuracy\": 100,\"pp\": 15,\"description\": \"A strong electric attack\"}]";
        var types = "[{\"name\": \"Electric\"}]";
        var item = "{\"name\": \"Light Ball\",\"description\": \"A strange ball that boosts Pikachu's stats\"}";
        var stats = "[{\"name\": \"hp\",\"value\": 35,\"max\": 35},{\"name\": \"Attack\",\"value\": 55,\"max\": 55},{\"name\": \"Defense\",\"value\": 40,\"max\": 40},{\"name\": \"Sp. Attack\",\"value\": 50,\"max\": 50},{\"name\": \"Sp. Defense\",\"value\": 50,\"max\": 50},{\"name\": \"Speed\",\"value\": 90,\"max\": 90}]";
        var ability = "{\"name\": \"Static\",\"description\": \"May cause paralysis if touched\"}";
        var pokemon = "{\"name\": \"Pikachu\",\"type\": " + types + ",\"level\": 5,\"gender\": \"Male\",\"isShiny\": false,\"id\": 25,\"Description\": \"Mouse Pokemon\",\"ability\": " + ability + ",\"nature\": \"Brave\",\"moves\": " + moves + ",\"item\": " + item + ",\"stats\": " + stats + "}";
        var response = mockMvc.perform(post("/teams/Team Rocket/pokemons/Pikachu").contentType(MediaType.APPLICATION_JSON).content(pokemon));

        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("Team not found"));
    }

    @Test
    public void addPokemonReturnConflictStatus() throws Exception {
        // Given
        var moves = "[{\"name\": \"Thunderbolt\",\"type\": \"Electric\",\"category\": \"Special\",\"power\": 90,\"accuracy\": 100,\"pp\": 15,\"description\": \"A strong electric attack\"}]";
        var type = "[{\"name\": \"Electric\"}]";
        var item = "{\"name\": \"Light Ball\",\"description\": \"A strange ball that boosts Pikachu's stats\"}";
        var stats = "[{\"name\": \"hp\",\"value\": 35,\"max\": 35},{\"name\": \"Attack\",\"value\": 55,\"max\": 55},{\"name\": \"Defense\",\"value\": 40,\"max\": 40},{\"name\": \"Sp. Attack\",\"value\": 50,\"max\": 50},{\"name\": \"Sp. Defense\",\"value\": 50,\"max\": 50},{\"name\": \"Speed\",\"value\": 90,\"max\": 90}]";
        var ability = "{\"name\": \"Static\",\"description\": \"May cause paralysis if touched\"}";
        var pokemon = "{\"name\": \"Pikachu\",\"type\": " + type + ",\"level\": 5,\"gender\": \"Male\",\"isShiny\": false,\"id\": 25,\"ability\": " + ability + ",\"nature\": \"Brave\",\"moves\": " + moves + ",\"item\": " + item + ",\"stats\": " + stats + "}";
        var avatar = "{\"name\": \"Red\",\"location\": \"Kanto\",\"url\": \"~/public/images/compressed/avatars/kanto/red.png\"}";
        mockMvc.perform(post("/teams")
                .content("{\"name\": \"Team Red\",\"avatar\": " + avatar + "}")
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(post("/teams/Team Red/pokemons/Pikachu").contentType(MediaType.APPLICATION_JSON).content(pokemon));

        // When

        var response = mockMvc.perform(post("/teams/Team Red/pokemons/Pikachu").contentType(MediaType.APPLICATION_JSON).content(pokemon));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Pokemon already exists"));
    }
}
