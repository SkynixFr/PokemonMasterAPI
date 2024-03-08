package org.example.pokemonmasterapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.example.pokemonmasterapi.classes.Salutations;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    @Operation(hidden = true)
    public Salutations helloWorld(@RequestParam String name, @RequestParam(defaultValue = "en") String lang)
    {
        if (name.isEmpty()) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        if (Objects.equals(lang, "fr")){
            return new Salutations("Bonjour, " + name + "!", lang);
        }
        return new Salutations("Hello, " + name + "!", lang);
    }
}