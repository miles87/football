package com.michonski.football.service;

import com.michonski.football.dto.PlayerDto;
import com.michonski.football.dto.TeamDto;
import com.michonski.football.exception.AppException;
import com.michonski.football.model.Player;
import com.michonski.football.model.Team;
import com.michonski.football.repository.PlayerRepository;
import com.michonski.football.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;


    public PlayerDto add(PlayerDto playerDto) {

        if (playerDto == null) {
            throw new AppException("Player shouldn't be null");
        }
        if (playerDto.getTeamDto() == null) {
            throw new AppException("Player should have a team");
        }
        Team team = null;

        if (playerDto.getTeamDto().getId() != null) {
            team = teamRepository.findById(playerDto.getTeamDto().getId()).orElse(null);
        }

        if (team == null) // nie udalo sie znalezc po id wiec probujemy szukac po name
        {
            team = teamRepository
                    .findByName(playerDto.getTeamDto().getName())
                    .orElseThrow(() -> new AppException("Team should have name"));
        }
        Player player = ModelMapper.fromPlayerDtoToPlayer(playerDto);
        player.setTeam(team);
        Player player1 = playerRepository.save(player);
        return ModelMapper.fromPlayerToPlayerDto(player1);
    }

    public PlayerDto findOne(Long id) {

        if (id == null) {
            throw new AppException("id shouldn't be null");
        }

        return playerRepository
                .findById(id)
                .map(ModelMapper::fromPlayerToPlayerDto)
                .orElseThrow(() -> new AppException("there is no player with this id"));
    }

    public List<PlayerDto> findAll() {
        return playerRepository.findAll()
                .stream()
                .map(ModelMapper::fromPlayerToPlayerDto)
                .collect(Collectors.toList());
    }

    public PlayerDto deletePlayer(Long id) {
        if (id == null) {
            throw new AppException("id shouldn't be null");
        }
        PlayerDto playerDto = playerRepository.findById(id)
                .map(ModelMapper::fromPlayerToPlayerDto)
                .orElseThrow(() -> new AppException("there is no player with this id"));
        playerRepository.deleteById(id);
        return playerDto;
    }

    public PlayerDto updatePlayer(Long id, PlayerDto playerDto) {

        if (id == null) {
            throw new AppException("id shouldn't be null");
        }

        if (playerDto == null) {
            throw new AppException("player with update data shouldn't be null");
        }

        Player playerFromDb = playerRepository.findById(id)
                .orElseThrow(() -> new AppException("there is no player with this id"));

        playerFromDb.setFirstName(playerDto.getFirstName() == null ? playerFromDb.getFirstName() : playerDto.getFirstName());
        playerFromDb.setLastName(playerDto.getLastName() == null ? playerFromDb.getLastName() : playerDto.getLastName());
        playerFromDb.setBirthDate(playerDto.getBirthDate() == null ? playerFromDb.getBirthDate() : playerDto.getBirthDate());
        playerFromDb.setNumber(playerDto.getNumber() == null ? playerFromDb.getNumber() : playerDto.getNumber());
        playerFromDb.setPrice(playerDto.getPrice() == null ? playerFromDb.getPrice() : playerDto.getPrice());

        if (playerDto.getTeamDto() != null) {
            TeamDto teamDto = playerDto.getTeamDto();

            if (teamDto.getId() != null) {
                Team team = teamRepository.findById(teamDto.getId()).orElseThrow(() ->new AppException("there is no team with this id"));
                playerFromDb.setTeam(team);
            } else if (teamDto.getName() != null) {
                Team team = teamRepository.findByName(teamDto.getName()).orElseThrow(() -> new AppException("no team with given name"));
                playerFromDb.setTeam(team);
            }
        }

        return ModelMapper.fromPlayerToPlayerDto(playerRepository.save(playerFromDb));

    }


}
