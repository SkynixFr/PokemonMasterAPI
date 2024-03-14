package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Car;
import org.example.pokemonmasterapi.repositories.CarRepository;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CarController {
    private final CarRepository carRepository;

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carRepository.findByBrand("Ferrari");
    }

    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public Car addCar(@RequestBody Car car) {
        return carRepository.save(car);
    }
}
