package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.model.Car;
import org.example.pokemonmasterapi.repositories.CarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarResourceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void tearDown() {
        carRepository.deleteAll();
    }

    @Test
    public void whenResourceGetThenContentIsAsExpected() throws Exception{
        //Given
        carRepository.save(new Car(1, "Ferrari"));
        carRepository.save(new Car(2, "Porsche"));

        //When
        var actualResponse = mockMvc.perform(get("/cars"));

        //Then
        actualResponse.andExpect(status().isOk());
        actualResponse.andExpect(content().json("[{\"id\":1,\"brand\":\"Ferrari\"}]"));
    }

    @Test
    public void whenResourceGetThenContentIsAdded() throws Exception{
        //Given

        //When
        var actualResponse = mockMvc.perform(post("/cars").content("{\"id\":1,\"brand\":\"Ferrari\"}").contentType(MediaType.APPLICATION_JSON));

        //Then
        actualResponse.andExpect(status().isCreated());
        actualResponse.andExpect(jsonPath("$.brand").value("Ferrari"));

        assertThat(carRepository.findAll()).hasSize(1).first().satisfies(car -> {
            assertThat(car.getId()).isNotNull();
            assertThat(car.getBrand()).isEqualTo("Ferrari");
        });
    }
}
