package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.model.Avatar;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/avatars")
@CrossOrigin(origins = "http://localhost:3000")
public class AvatarController {
    private final AvatarRepository avatarRepository;

    @GetMapping
    public ResponseEntity<Object> getAvatars() {
        return ResponseEntity.status(HttpStatus.OK).body(avatarRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> addAvatar(@RequestBody Avatar avatar) {
        if (avatar.getName() == null || avatar.getLocation() == null || avatar.getUrl() == null || avatar.getName().isEmpty() || avatar.getLocation().isEmpty() || avatar.getUrl().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing name, location or url");
        }

        if (!avatarRepository.findByName(avatar.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Avatar already exists");
        }

        avatarRepository.save(avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body("Avatar created");
    }
}
