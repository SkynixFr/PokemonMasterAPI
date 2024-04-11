package org.example.pokemonmasterapi.controllers;

import lombok.AllArgsConstructor;
import org.example.pokemonmasterapi.controllers.model.TeamCreate;
import org.example.pokemonmasterapi.controllers.model.TeamResponse;
import org.example.pokemonmasterapi.controllers.model.TeamUpdate;
import org.example.pokemonmasterapi.repositories.AvatarRepository;
import org.example.pokemonmasterapi.repositories.TeamRepository;
import org.example.pokemonmasterapi.repositories.model.TeamEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    private final TeamRepository teamRepository;
    private final AvatarRepository avatarRepository;

    public TeamResponse mappingTeamResponse(TeamEntity team) {
        var avatar = avatarRepository.findById(team.getAvatarId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Avatar does not exist"));
        return new TeamResponse(team.getId(), team.getName(), avatar, team.getPokemons());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse addTeam(@RequestBody @Validated TeamCreate team) {
        if (teamRepository.existsByName(team.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Team with name " + team.getName() + " already exists");
        }

        var avatar = avatarRepository.findById(team.getAvatarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar does not exist"));

        var teamBDD = teamRepository.save(new TeamEntity(null, team.getName(), avatar.getId(), null));

        return new TeamResponse(teamBDD.getId(), teamBDD.getName(), avatar, teamBDD.getPokemons());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TeamResponse> getTeams() {
        var teams = teamRepository.findAll();
        return teams.stream()
                .map(this::mappingTeamResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse getTeam(@PathVariable String id) {

        var team = teamRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        return this.mappingTeamResponse(team);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable String id) {
        teamRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        teamRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse saveTeam(@PathVariable String id, @RequestBody @Validated TeamUpdate team) {
        if (!teamRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }

        var avatar = avatarRepository.findById(team.getAvatarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar does not exist"));

        var newTeam = teamRepository.save(new TeamEntity(id, team.getName(), avatar.getId(), team.getPokemons()));

        return new TeamResponse(newTeam.getId(), newTeam.getName(), avatar, newTeam.getPokemons());
    }
}