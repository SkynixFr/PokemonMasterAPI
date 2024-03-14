package org.example.pokemonmasterapi.repositories;

import org.example.pokemonmasterapi.model.Car;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;


public interface CarRepository extends ListCrudRepository<Car, Integer> {
    List<Car> findByBrand(String brand);
}
