package org.example.pokemonmasterapi;

import org.example.pokemonmasterapi.repositories.PokemonRepository;
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
public class PokemonTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PokemonRepository pokemonRepository;

    @AfterEach
    public void tearDown() {
        pokemonRepository.deleteAll();
    }

    @Test
    public void addPokemonReturnCreatedStatus() throws Exception {
        // Given

        // When
        var response = mockMvc.perform(post("/pokemons")
                .content("{" +
                        "  \"pokedexId\": 1," +
                        "  \"name\": \"Bulbasaur\"," +
                        "  \"types\": [" +
                        "    {" +
                        "      \"name\": \"Grass\"," +
                        "      \"damageRelation\": {" +
                        "        \"doubleDamageFrom\": [\"Fire\"]," +
                        "        \"doubleDamageTo\": [\"Water\"]," +
                        "        \"halfDamageFrom\": [\"Electric\"]," +
                        "        \"halfDamageTo\": [\"Rock\"]," +
                        "        \"noDamageFrom\": [\"Grass\"]," +
                        "        \"noDamageTo\": [\"Ghost\"]" +
                        "      }" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Poison\"," +
                        "      \"damageRelation\": {" +
                        "        \"doubleDamageFrom\": [\"Ground\"]," +
                        "        \"doubleDamageTo\": [\"Psychic\"]," +
                        "        \"halfDamageFrom\": [\"Bug\"]," +
                        "        \"halfDamageTo\": [\"Fighting\"]," +
                        "        \"noDamageFrom\": [\"Poison\"]," +
                        "        \"noDamageTo\": [\"Steel\"]" +
                        "      }" +
                        "    }" +
                        "  ]," +
                        "  \"gender\": \"male\"," +
                        "  \"stats\": [" +
                        "    {\"name\": \"hp\", \"value\": 45, \"max\": 100}," +
                        "    {\"name\": \"attack\", \"value\": 49, \"max\": 100}," +
                        "    {\"name\": \"defense\", \"value\": 49, \"max\": 100}," +
                        "    {\"name\": \"specialAttack\", \"value\": 65, \"max\": 100}," +
                        "    {\"name\": \"specialDefense\", \"value\": 65, \"max\": 100}," +
                        "    {\"name\": \"speed\", \"value\": 45, \"max\": 100}" +
                        "  ]," +
                        "  \"weight\": 69" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isCreated());
    }

    @Test
    public void getPokemonsReturnOkStatus() throws Exception {
        // Given
        mockMvc.perform(post("/pokemons")
                .content("{" +
                        "  \"pokedexId\": 1," +
                        "  \"name\": \"Bulbasaur\"," +
                        "  \"types\": [" +
                        "    {" +
                        "      \"name\": \"Grass\"," +
                        "      \"damageRelation\": {" +
                        "        \"doubleDamageFrom\": [\"Fire\"]," +
                        "        \"doubleDamageTo\": [\"Water\"]," +
                        "        \"halfDamageFrom\": [\"Electric\"]," +
                        "        \"halfDamageTo\": [\"Rock\"]," +
                        "        \"noDamageFrom\": [\"Grass\"]," +
                        "        \"noDamageTo\": [\"Ghost\"]" +
                        "      }" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Poison\"," +
                        "      \"damageRelation\": {" +
                        "        \"doubleDamageFrom\": [\"Ground\"]," +
                        "        \"doubleDamageTo\": [\"Psychic\"]," +
                        "        \"halfDamageFrom\": [\"Bug\"]," +
                        "        \"halfDamageTo\": [\"Fighting\"]," +
                        "        \"noDamageFrom\": [\"Poison\"]," +
                        "        \"noDamageTo\": [\"Steel\"]" +
                        "      }" +
                        "    }" +
                        "  ]," +
                        "  \"gender\": \"male\"," +
                        "  \"stats\": [" +
                        "    {\"name\": \"hp\", \"value\": 45, \"max\": 100}," +
                        "    {\"name\": \"attack\", \"value\": 49, \"max\": 100}," +
                        "    {\"name\": \"defense\", \"value\": 49, \"max\": 100}," +
                        "    {\"name\": \"specialAttack\", \"value\": 65, \"max\": 100}," +
                        "    {\"name\": \"specialDefense\", \"value\": 65, \"max\": 100}," +
                        "    {\"name\": \"speed\", \"value\": 45, \"max\": 100}" +
                        "  ]," +
                        "  \"weight\": 69" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));

        // When
        var response = mockMvc.perform(get("/pokemons"));

        // Then
        response.andExpect(status().isOk());
        assertThat(pokemonRepository.findAll()).hasSize(1);
        assertThat(response.andReturn().getResponse().getContentAsString()).contains("Bulbasaur");
    }
}
