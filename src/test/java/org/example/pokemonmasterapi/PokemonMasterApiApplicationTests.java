package org.example.pokemonmasterapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PokemonMasterApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void whenHelloApiCalledWithNameThenContentISHelloName() throws Exception {
        //Given

        //When
        var actualResponse = mockMvc.perform(get("/hello").queryParam("name", "Cocoloco"));
        //Then
        actualResponse.andExpect(status().isOk()).andExpect(content().json("{\"message\":\"Hello, Cocoloco!\",\"lang\":\"en\"}"));
    }

    @Test
    public void whenHelloApiCalledWithoutNameThenContentISEmpty() throws Exception {
        //Given

        //When
        var actualResponse = mockMvc.perform(get("/hello").queryParam("name", ""));
        //Then
        actualResponse.andExpect(status().isBadRequest());
    }

    @Test
    public void whenHelloApiCalledWithNameThenContentISFrench() throws Exception {
        //Given

        //When
        var actualResponse = mockMvc.perform(get("/hello").queryParam("name", "Cocoloco").queryParam("lang", "fr"));
        //Then
        actualResponse.andExpect(status().isOk()).andExpect(content().json("{\"message\":\"Bonjour, Cocoloco!\",\"lang\":\"fr\"}"));
    }

}
