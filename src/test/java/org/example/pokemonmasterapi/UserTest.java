package org.example.pokemonmasterapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pokemonmasterapi.controllers.model.UserResponse;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.example.pokemonmasterapi.repositories.model.AvatarEntity;
import org.example.pokemonmasterapi.repositories.model.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
        avatarRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void RegisterUserReturnCreatedStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"avatarId\":\"1\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        var responseID = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/id").asText();
        var userBDD = userRepository.findById(responseID).get();
        assertTrue(passwordEncoder.matches("JesuisunMDP-33", userBDD.getPassword()));
        response.andExpect(status().isCreated());
        response.andExpect(content().json(objectMapper.writeValueAsString(new UserResponse(responseID, "Ricky","IlovePikachu@gmail.com",
                userBDD.getPassword(),
                new AvatarEntity("1","Team Red","Kanto","~/public/images/compressed/avatars/kanto/red.png"), null, "USER"))));
    }

    @Test
    public void RegisterUserReturnConflictStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", "JesuisunMDP-33","1", null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"avatarId\":\"1\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(status().reason("User with email IlovePikachu@gmail.com already exists"));
    }

    @Test
    public void RegisterUserWithEmailInvalidReturnBadRequestStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu\",\"avatarId\":\"1\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUserWithRoleInvalidReturnBadRequestStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content(
                                "{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmaiL.com\",\"avatarId\":\"1\",\"role\": \"USER2\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUserWithUsernameInvalidReturnBadRequestStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        // When
        var response = mockMvc.perform(
                post("/user/register")
                        .content(
                                "{\"username\": \"\",\"password\": \"JesuisunMDP-33\",\"email\": \"IlovePikachu@gmail.com\",\"avatarId\":\"1\",\"role\": \"USER\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void LoginUserReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),"1", null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().string(containsString("accessToken")));
        response.andExpect(content().string(containsString("refreshToken")));
    }

    @Test
    public void LoginUserWithInvalidEmailReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        // When
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33")
                        , "1", null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu1@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    public void LoginUserWithInvalidPasswordReturnUnauthorizedStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP@34\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void LoginUserWithEmailInvalidReturnBadRequestStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void LoginUserWithPasswordInvalidReturnBadRequestStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test public void DeleteUserReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        // When
        var response = mockMvc.perform(delete("/user/1"));
        // Then
        response.andExpect(status().isOk());
    }

    @Test public void DeleteUserWithInvalidIdReturnNotFoundStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(delete("/user/1"));
        // Then
        response.andExpect(status().isNotFound());
    }

    @Test public void MeReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("2", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        // When
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at("/accessToken").asText();
        var response2 = mockMvc.perform(
                get("/user/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        var responseID = objectMapper.readTree(response2.andReturn().getResponse().getContentAsString()).at(
                "/id").asText();
        var userBDD = userRepository.findById(responseID).get();
        assertTrue(passwordEncoder.matches("JesuisunMDP-33", userBDD.getPassword()));
        response2.andExpect(status().isOk());
        response2.andExpect(content().json(objectMapper.writeValueAsString(new UserResponse("2", "Luffysonic","IlovePikachu@gmail.com",userBDD.getPassword(),
                new AvatarEntity("1","Team Red","Kanto","~/public/images/compressed/avatars/kanto/red.png"), null, "USER"))));
    }

    @Test public void MeWithInvalidTokenReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("2", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at("/accessToken").asText();
        userRepository.deleteAll();
        //When
        var response2 = mockMvc.perform(
                get("/user/me")
                        .header("Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isNotFound());
    }

    @Test public void UpdateUserReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("2", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at("/accessToken").asText();
        // When
        var response2 = mockMvc.perform(
                put("/user/2")
                        .header("Authorization", "Bearer " + token)
                        .content("{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-34\",\"email\": \"IloveRaichu@gmail.com\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        var responseID = objectMapper.readTree(response2.andReturn().getResponse().getContentAsString()).at(
                "/id").asText();
        var userBDD = userRepository.findById(responseID).get();
        assertTrue(passwordEncoder.matches("JesuisunMDP-34", userBDD.getPassword()));
        response2.andExpect(status().isOk());
        response2.andExpect(content().json(objectMapper.writeValueAsString(new UserResponse("2", "Ricky","IloveRaichu@gmail.com",userBDD.getPassword(),
                new AvatarEntity("1","Team Red","Kanto","~/public/images/compressed/avatars/kanto/red.png"), null, "USER"))));
    }

    @Test public void UpdateUserWithInvalidIdReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at("/accessToken").asText();
        userRepository.deleteAll();
        // When
        var response2 = mockMvc.perform(
                put("/user/1")
                        .header("Authorization", "Bearer " + token)
                        .content(
                                "{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-34\",\"email\": \"IlovePikachu@gmail.com\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isNotFound());
    }

    @Test public void UpdateUserWithInvalidTokenReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/accessToken").asText();
        // When
        var response2 = mockMvc.perform(
                put("/user/1")
                        .header("Authorization", "Bearer " + token)
                        .content(
                                "{\"username\": \"\",\"password\": \"\",\"email\": \"\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isBadRequest());
    }

    @Test
    public void UpdateUserReturnConflictStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var token = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/accessToken").asText();
        // When
        var response2 = mockMvc.perform(
                put("/user/1")
                        .header("Authorization", "Bearer " + token)
                        .content(
                                "{\"username\": \"Ricky\",\"password\": \"JesuisunMDP-34\",\"email\": \"IlovePikachu@gmail.com\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isConflict());
        response2.andExpect(status().reason("User with email IlovePikachu@gmail.com already exists"));
    }

    @Test
    public void GetAnewTokenReturnOkStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var refreshToken = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/refreshToken").asText();
        // When
        var response2 = mockMvc.perform(
                post("/user/refreshToken")
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isOk());
        response2.andExpect(content().string(containsString("accessToken")));
    }

    @Test
    public void GetAnewTokenWithInvalidTokenReturnNotFoundStatus() throws Exception {
        // Given
        avatarRepository.save(
                new AvatarEntity("1", "Team Red", "Kanto", "~/public/images/compressed/avatars/kanto/red.png"));
        userRepository.save(
                new UserEntity("1", "Luffysonic", "IlovePikachu@gmail.com", passwordEncoder.encode("JesuisunMDP-33"),
                        "1",null, "USER"));
        var response = mockMvc.perform(
                post("/user/login")
                        .content("{\"email\": \"IlovePikachu@gmail.com\",\"password\": \"JesuisunMDP-33\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        var refreshToken = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).at(
                "/refreshToken").asText();
        userRepository.deleteAll();
        // When
        var response2 = mockMvc.perform(
                post("/user/refreshToken")
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response2.andExpect(status().isNotFound());
    }

    @Test
    public void GetAnewTokenWithInvalidRefreshTokenReturnInvalidStatus() throws Exception {
        // Given
        // When
        var response = mockMvc.perform(
                post("/user/refreshToken")
                        .header("Authorization", "Bearer " + " ")
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isUnauthorized());
    }



}
