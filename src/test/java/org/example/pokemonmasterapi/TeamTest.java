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

        var pokemons = List.of(
                new PokemonTeamEntity(25,
                        "Pikachu",
                        List.of(new TypeEntity("Electric", new DamageRelationEntity(
                                List.of("Ground"),
                                List.of("Flying", "Water"),
                                List.of("Electric"),
                                List.of("Electric", "Flying", "Steel"),
                                List.of("Ground"),
                                List.of("Ground", "Grass")
                        ))),
                        1,
                        new AbilityEntity(null, "Static", "May cause paralysis if touched", List.of("Pikachu")),
                        new NatureEntity(null, "Modest", "Sp. Attack", "Attack"),
                        GenderEnum.male,
                        false,
                        List.of(
                                new MoveEntity(null, "Thunderbolt", 90, 100, 15,
                                        new MetaEntity("paralysis", 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0), "Electric",
                                        "special", "A strong electric attack", List.of("Pikachu"), null,
                                        "selected-pokemons")
                        )
                        ,
                        new ItemEntity(null, "Light Ball", "A strange ball that boosts Pikachu's stats", "/images/"),
                        List.of(
                                new StatEntity("hp", 35, 35, 0, 0, 0),
                                new StatEntity("Attack", 55, 55, 0, 0, 0),
                                new StatEntity("Defense", 40, 40, 0, 0, 0),
                                new StatEntity("Sp. Attack", 50, 50, 0, 0, 0),
                                new StatEntity("Sp. Defense", 50, 50, 0, 0, 0),
                                new StatEntity("Speed", 90, 90, 0, 0, 0),
                                new StatEntity("Accuracy", 100, 100, 0, 0, 0),
                                new StatEntity("critRate", 4.17, 100, 0, 0, 0),
                                new StatEntity("evasion", 0, 100, 0, 0, 0)
                        ),
                        60)
        );

        // When
        var responseUpdate = mockMvc.perform(
                put("/teams/" + teamId).content(
                        "{\"name\": \"Team Red\",\"avatarId\":\"1\",\"pokemons\": " + objectMapper.writeValueAsString(
                                pokemons) + "}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        responseUpdate.andExpect(status().isCreated());
        responseUpdate.andExpect(
                content().json(objectMapper.writeValueAsString(new TeamResponse(teamId, "Team Red",
                        new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"),
                        pokemons))));
    }
}
