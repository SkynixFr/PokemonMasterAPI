package org.example.pokemonmasterapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pokemonmasterapi.controllers.model.TeamResponse;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TeamTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
        avatarRepository.deleteAll();
    }

    @Test
    public void addTeamReturnCreatedStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));

        // When

        var response = mockMvc.perform(
                post("/teams").content("{\"name\": \"Team Red\",\"avatarId\":\"1\"}").contentType(
                        MediaType.APPLICATION_JSON));

        var responseID = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/id").asText();
        // Then
        response.andExpect(status().isCreated());
        response.andExpect(content().json(objectMapper.writeValueAsString(new TeamResponse(responseID, "Team Red",
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"),
                null))));
    }

    @Test
    public void addTeamReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(
                post("/teams").content("{\"name\": \"Team Red\"}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void addTeamReturnConflictStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        mockMvc.perform(
                post("/teams").content("{\"name\": \"Team Red\",\"avatarId\":\"1\"}").contentType(
                        MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(
                post("/teams").content("{\"name\": \"Team Red\",\"avatarId\":\"1\"}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(status().reason("Team with name Team Red already exists"));

    }

    @Test
    public void returnAllTeamsOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        teamRepository.save(new TeamEntity(null, "Team Red", "1", null));
        teamRepository.save(new TeamEntity(null, "Team Cynthia", "1", null));

        // When
        var response = mockMvc.perform(get("/teams"));

        // Then
        response.andExpect(status().isOk());
        assertThat(teamRepository.findAll()).hasSize(2);
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Team Red", "Team Cynthia");
    }

    @Test
    public void returnOneTeamOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        // When
        var responseOneTeam = mockMvc.perform(get("/teams/" + teamId));

        // Then
        responseOneTeam.andExpect(status().isOk());
        assertThat(responseOneTeam.andReturn().getResponse().getContentAsString()).contains("Team Red");
    }

    @Test
    public void returnOneTeamNotFoundStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(get("/teams/Team Rocket"));

        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(status().reason("Team not found"));
    }

    @Test
    public void deleteTeamReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        // When
        var responseDelete = mockMvc.perform(delete("/teams/" + teamId));

        // Then
        responseDelete.andExpect(status().isNoContent());
    }


    @Test
    public void deleteTeamReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        // When
        mockMvc.perform(delete("/teams/" + teamId));
        var responseDelete = mockMvc.perform(delete("/teams/" + teamId));

        // Then
        responseDelete.andExpect(status().isNotFound());
        responseDelete.andExpect(status().reason("Team not found"));
    }

    @Test
    public void saveTeamReturnCreatedStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        var moves = "[{\"name\": \"Thunderbolt\",\"type\": \"Electric\",\"category\": \"Special\",\"power\": 90,\"accuracy\": 100,\"pp\": 15,\"description\": \"A strong electric attack\"}]";
        var types = "[{\"name\": \"Electric\"}]";
        var item = "{\"name\": \"Light Ball\",\"description\": \"A strange ball that boosts Pikachu's stats\",\"image\": \"/images/\"}";
        var stats = "[{\"name\": \"hp\",\"value\": 35,\"max\": 35},{\"name\": \"Attack\",\"value\": 55,\"max\": 55},{\"name\": \"Defense\",\"value\": 40,\"max\": 40},{\"name\": \"Sp. Attack\",\"value\": 50,\"max\": 50},{\"name\": \"Sp. Defense\",\"value\": 50,\"max\": 50},{\"name\": \"Speed\",\"value\": 90,\"max\": 90}]";
        var ability = "{\"name\": \"Static\",\"description\": \"May cause paralysis if touched\"}";
        var pokemons = "[{\"name\": \"Pikachu\",\"types\": " + types + ",\"level\": 5,\"gender\": \"Male\",\"isShiny\": false,\"id\": 25,\"Description\": \"Mouse Pokemon\",\"ability\": " + ability + ",\"nature\": \"Brave\",\"moves\": " + moves + ",\"item\": " + item + ",\"stats\": " + stats + "}]";

        // When
        var responseUpdate = mockMvc.perform(
                put("/teams/" + teamId).content(
                        "{\"name\": \"Team Red\",\"avatarId\":\"1\",\"pokemons\": " + pokemons + "}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        responseUpdate.andExpect(status().isCreated());
        responseUpdate.andExpect(
                content().json(objectMapper.writeValueAsString(new TeamResponse(teamId, "Team Red",
                        new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"),
                        List.of(
                                new PokemonEntity(
                                        "Pikachu",
                                        new TypeEntity[]{new TypeEntity("Electric")},
                                        5,
                                        new AbilityEntity("Static", "May cause paralysis if touched"),
                                        "Brave",
                                        GenderEnum.Male,
                                        false,
                                        25,
                                        new MoveEntity[]{
                                                new MoveEntity("Thunderbolt", "Electric", "Special", 90, 100, 15,
                                                        "A strong electric attack")
                                        },
                                        new ItemEntity(
                                                "Light Ball",
                                                "A strange ball that boosts Pikachu's stats",
                                                "/images/"
                                        ),
                                        new StatEntity[]{
                                                new StatEntity("hp", 35, 35),
                                                new StatEntity("Attack", 55, 55),
                                                new StatEntity("Defense", 40, 40),
                                                new StatEntity("Sp. Attack", 50, 50),
                                                new StatEntity("Sp. Defense", 50, 50),
                                                new StatEntity("Speed", 90, 90)
                                        }
                                ))
                ))));
    }
}
