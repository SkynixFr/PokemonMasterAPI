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

import java.util.ArrayList;
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
                new ArrayList<>()))));
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
    public void saveTeamReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        // When
        var responseSave = mockMvc.perform(
                put("/teams/" + teamId).content("{\"name\": \"Team Rocket\",\"avatarId\":\"1\"}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        responseSave.andExpect(status().isCreated());
        assertThat(teamRepository.findById(teamId).get().getName()).isEqualTo("Team Rocket");
    }

    @Test
    public void saveTeamWithPokemonsReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        var teamId = teamRepository.save(new TeamEntity(null, "Team Red", "1", null)).getId();

        // When
        var pokemon = new PokemonTeamEntity(1, "Bulbasaur",
                List.of(new TypeEntity("Grass", new DamageRelationEntity(List.of("Fire"), List.of("Water"), List.of("Electric"), List.of("Rock"), List.of("Grass"), List.of("Ghost")))),
                        45,
                        new AbilityEntity("1","Overgrow", "When HP is below 1/3, Grass's power increases by 50%", List.of("Bulbasaur")),
                        new NatureEntity("1", "Modest", "Special Attack", "Attack"),
                        GenderEnum.neutral,
                        false,
                        List.of(new MoveEntity("1","Tackle",40, 100, 35,
                                new MetaEntity("None", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0),
                                "Normal", "Physical","Description", List.of("Bulbasaur"), null, "user" )),
                        new ItemEntity("1", "None", "No item", "No item"),
                        List.of(new StatEntity("hp", 45, 100, 0, 0, 100, 45),
                                new StatEntity("attack", 49, 100, 0, 0, 100, 49),
                                new StatEntity("defense", 49, 100, 0, 0, 100, 49),
                                new StatEntity("special-attack", 65, 100, 0, 0, 100, 65),
                                new StatEntity("special-defense", 65, 100, 0, 0, 100, 65),
                                new StatEntity("speed", 45, 100, 0, 0, 100, 45)),
                        69);

        var responseSave = mockMvc.perform(
                put("/teams/" + teamId).content("{\"name\": \"Team Rocket\",\"avatarId\":\"1\",\"pokemons\":[" + objectMapper.writeValueAsString(pokemon) + "]}").contentType(
                        MediaType.APPLICATION_JSON));

        // Then
        responseSave.andExpect(status().isCreated());
        assertThat(teamRepository.findById(teamId).get().getName()).isEqualTo("Team Rocket");
        assertThat(teamRepository.findById(teamId).get().getPokemons()).hasSize(1);
    }
}
