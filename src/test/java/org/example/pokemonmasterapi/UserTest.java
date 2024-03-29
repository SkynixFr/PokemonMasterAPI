package org.example.pokemonmasterapi;

import com.jayway.jsonpath.JsonPath;
import org.example.pokemonmasterapi.model.User;
import org.example.pokemonmasterapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.*;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    public void registerUserReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\",\"email\": \"IlovePikachu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isCreated());

    }

    @Test
    public void registerUserReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Missing required data. Please provide email, username, and password"));
    }

    @Test
    public void registerUserReturnConflictStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword("Pikachu");
        user.setEmail("ILovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\",\"email\": \"IlovePichu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Username already exists"));
    }

    @Test
    public void registerUserReturnConflictStatus2() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword("Pikachu");
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/register")
                .content("{\"username\": \"Ricky2\",\"password\": \"Pikachu\",\"email\": \"IlovePikachu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Email already exists"));
    }

    @Test
    public void loginUserReturnOkStatusAndAccessToken() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

          user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    public void loginUserReturnBadRequestStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void loginUserReturnNotFoundStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("User not found"));
    }

    @Test
    public void loginUserReturnUnauthorizedStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var response = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu2\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isUnauthorized());
        response.andExpect(content().string("Password is incorrect"));
    }

    @Test
    public void deleteUserReturnOkStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var accessToken = login.andReturn().getResponse().getContentAsString();
        var id = userRepository.findByUsername("Ricky").get().getId();
        // When
        var response = mockMvc.perform(delete("/user/" + id)
                .header("Authorization", accessToken));
        // Then
        response.andExpect(status().isOk());
        // Check if the user is deleted
        assert userRepository.findByUsername("Ricky").isEmpty();
    }

    @Test
    public void deleteUserReturnNotFoundStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var accessToken = login.andReturn().getResponse().getContentAsString();
        var id = userRepository.findByUsername("Ricky").get().getId();
        userRepository.deleteById(user.getId());
        // When
        var response = mockMvc.perform(delete("/user/123")
                .header("Authorization", accessToken));
        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("User not found"));
    }

    @Test
    public void deleteUserReturnUnauthorizedStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("iLovePikachu");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var accessToken = login.andReturn().getResponse().getContentAsString();
        // When
        var response = mockMvc.perform(delete("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken));
        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteUserReturnBadRequestStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("iLovePikachu");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(delete("/user/")
                .header("Authorization", "Bearer " + accessToken));
        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void meReturnOkStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");


        // When
        var response = mockMvc.perform(get("/user/me")
                .header("Authorization", "Bearer " + accessToken));
        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().string(containsString("Ricky")));
        response.andExpect(content().string(containsString("IlovePikachu@gmail.com")));
    }

    @Test
    public void meReturnNotFoundStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        // Extract the access token from the response
        String accessToken = JsonPath.parse(Body).read("$.accessToken");
        userRepository.deleteById(user.getId());
        // When
        var response = mockMvc.perform(get("/user/me")
                .header("Authorization", "Bearer " + accessToken));
        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("User not found"));
    }

    @Test
    public void meReturnUnauthorizedStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");

        user.setPassword(passwordEncoder.encode("Pikachu"));

        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        // Extract the access token from the response
        var accessToken = Body.substring(15, Body.length() - 1);
        // When
        var response = mockMvc.perform(get("/user/me")
                .header("Authorization", "Bearer " + accessToken + "2"));
        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void putWithoutIdReturnBadRequestStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(put("/user/")
                .header("Authorization", "Bearer " + accessToken));
        // Then
        response.andExpect(status().isBadRequest());
        response.andExpect(content().string("Missing id"));
    }

    @Test
    public void UpdateUserReturnOkStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(put("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken)
                .content("{\"username\": \"Ricky2\",\"password\": \"Pikachu\",\"email\": \"IlovePichu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().string(containsString("Ricky2")));
        response.andExpect(content().string(containsString("IlovePichu@gmail.com")));
        assert userRepository.findByUsername("Ricky2").isPresent();
    }

    @Test
    public void UpdateUserReturnNotFoundStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        userRepository.deleteById(user.getId());
        // When
        var response = mockMvc.perform(put("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken)
                .content("{\"username\": \"Ricky2\",\"password\": \"Pikachu\",\"email\": \"IlovePichu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("User not found"));
    }

    @Test
    public void UpdateUsernameReturnConflictStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(put("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken)
                .content("{\"username\": \"Ricky\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Username already exists"));
    }

    @Test
    public void UpdateEmailReturnConflictStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(put("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken)
                .content("{\"email\": \"IlovePikachu@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isConflict());
        response.andExpect(content().string("Email already exists"));
    }

    @Test
    public void UpdatePasswordReturnOkStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var accessToken = JsonPath.parse(Body).read("$.accessToken");
        // When
        var response = mockMvc.perform(put("/user/" + user.getId())
                .header("Authorization", "Bearer " + accessToken)
                .content("{\"password\": \"Pikachu2\"}")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andExpect(status().isOk());
    }

    @Test
    public void GetRefreshTokenReturnOkStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var refreshToken = JsonPath.parse(Body).read("$.refreshToken");
        // When
        var response = mockMvc.perform(get("/user/refreshToken")
                .header("Authorization", "Bearer " + refreshToken));
        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().string(containsString("accessToken")));
    }

    @Test
    public void GetRefreshTokenReturnUnauthorizedStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        // When
        var refreshToken = "eyJhbG";
        var response = mockMvc.perform(get("/user/refreshToken")
                .header("Authorization", "Bearer " + refreshToken));
        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void GetRefreshTokenReturnNotFoundStatus() throws Exception {
        // Given
        User user = new User();
        user.setUsername("Ricky");
        user.setPassword(passwordEncoder.encode("Pikachu"));
        user.setEmail("IlovePikachu@gmail.com");
        userRepository.save(user);
        var login = mockMvc.perform(post("/user/login")
                .content("{\"username\": \"Ricky\",\"password\": \"Pikachu\"}")
                .contentType(MediaType.APPLICATION_JSON));
        var Body = login.andReturn().getResponse().getContentAsString();
        var refreshToken = JsonPath.parse(Body).read("$.refreshToken");
        userRepository.deleteById(user.getId());
        // When
        var response = mockMvc.perform(get("/user/refreshToken")
                .header("Authorization", "Bearer " + refreshToken));
        // Then
        response.andExpect(status().isNotFound());
        response.andExpect(content().string("User not found"));
    }
}
