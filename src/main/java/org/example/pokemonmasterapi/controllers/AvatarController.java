package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.AvatarCreate;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.model.AvatarEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/avatars")
@CrossOrigin(origins = "http://localhost:3000")
public class AvatarController {
    private final AvatarRepository avatarRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AvatarEntity> getAvatars() {
        return avatarRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvatarEntity createAvatar(@RequestBody @Validated AvatarCreate avatar) {
        if (avatarRepository.existsByName(avatar.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Avatar with name " + avatar.getName() + " already exists");
        }

        return avatarRepository.save(new AvatarEntity(null, avatar.getName(), avatar.getRegion(), avatar.getSprite()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AvatarEntity updateAvatar(@PathVariable String id, @RequestBody @Validated AvatarCreate avatar) {
        if (!avatarRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Avatar with id " + id + " not found");
        }

        if (avatarRepository.existsByName(avatar.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Avatar with name " + avatar.getName() + " already exists");
        }

        return avatarRepository.save(new AvatarEntity(id, avatar.getName(), avatar.getRegion(), avatar.getSprite()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvatar(@PathVariable String id) {
        if (!avatarRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Avatar with id " + id + " not found");
        }

        avatarRepository.deleteById(id);
    }
}
