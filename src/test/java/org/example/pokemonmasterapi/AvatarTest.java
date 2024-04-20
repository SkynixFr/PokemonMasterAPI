package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.model.AvatarEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AvatarTest {
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
                .content(
                        "{\"name\": \"Red\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    public void addAvatarReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/avatars")
                .content("{\"name\":\"\",\"region\": \"\",\"sprite\": \"\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void addAvatarReturnConflictStatus() throws Exception {
        // Given
        mockMvc.perform(post("/avatars")
                .content(
                        "{\"name\": \"Red\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(post("/avatars")
                .content(
                        "{\"name\": \"Red\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isConflict());
        response.andExpect(status().reason("Avatar with name Red already exists"));
    }

    @Test
    public void getAvatarsReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/avatars")
                .content(
                        "{\"name\": \"Red\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/avatars"));

        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().json(
                "[{\"name\":\"Red\",\"region\":\"Kanto\",\"sprite\":\"~/public/images/compressed/avatars/kanto/Ash.png\"}]"));
    }

    @Test
    public void updateAvatarReturnOkStatus() throws Exception {
        //Given
        var avatarId = avatarRepository.save(
                new AvatarEntity(null, "Red", "Kanto", "~/public/images/compressed/avatars/kanto/Ash.png")).getId();

        //When
        var response = mockMvc.perform(put("/avatars/" + avatarId)
                .content(
                        "{\"name\": \"Blue\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Blue.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        //Then
        response.andExpect(status().isOk());
        response.andExpect(content().json(
                "{\"name\":\"Blue\",\"region\":\"Kanto\",\"sprite\":\"~/public/images/compressed/avatars/kanto/Blue.png\"}"));
    }

    @Test
    public void updateAvatarReturnConflictStatus() throws Exception {
        //Given
        avatarRepository.save(
                new AvatarEntity(null, "Red", "Kanto", "~/public/images/compressed/avatars/kanto/Ash.png"));
        var avatarId = avatarRepository.save(
                new AvatarEntity(null, "Blue", "Kanto", "~/public/images/compressed/avatars/kanto/Blue.png")).getId();

        //When
        var response = mockMvc.perform(put("/avatars/" + avatarId)
                .content(
                        "{\"name\": \"Red\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Ash.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        //Then
        response.andExpect(status().isConflict());
        response.andExpect(status().reason("Avatar with name Red already exists"));
    }

    @Test
    public void updateAvatarReturnNotFoundStatus() throws Exception {
        //Given
        var avatarId = avatarRepository.save(
                new AvatarEntity(null, "Red", "Kanto", "~/public/images/compressed/avatars/kanto/Ash.png")).getId();
        avatarRepository.deleteAll();

        //When
        var response = mockMvc.perform(put("/avatars/" + avatarId)
                .content(
                        "{\"name\": \"Blue\",\"region\": \"Kanto\",\"sprite\": \"~/public/images/compressed/avatars/kanto/Blue.png\"}")
                .contentType(MediaType.APPLICATION_JSON));

        //Then
        response.andExpect(status().isNotFound());
        response.andExpect(status().reason("Avatar with id " + avatarId + " not found"));
    }

    @Test
    public void deleteAvatarReturnNoContentStatus() throws Exception {
        //Given
        var avatarId = avatarRepository.save(
                new AvatarEntity(null, "Red", "Kanto", "~/public/images/compressed/avatars/kanto/Ash.png")).getId();

        //When
        var response = mockMvc.perform(delete("/avatars/" + avatarId));

        //Then
        response.andExpect(status().isNoContent());
    }

    @Test
    public void deleteAvatarReturnNotFoundStatus() throws Exception {
        //Given
        var avatarId = avatarRepository.save(
                new AvatarEntity(null, "Red", "Kanto", "~/public/images/compressed/avatars/kanto/Ash.png")).getId();

        //When
        mockMvc.perform(delete("/avatars/" + avatarId));
        var response = mockMvc.perform(delete("/avatars/" + avatarId));

        //Then
        response.andExpect(status().isNotFound());
    }
}
