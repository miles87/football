package com.michonski.football.service;

import com.michonski.football.dto.TeamDto;
import com.michonski.football.exception.AppException;
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
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;


    public List<TeamDto> findAll() {
        return teamRepository.findAll()
                .stream()
                .map(ModelMapper::fromTeamToTeamDto)
                .collect(Collectors.toList());
    }

    public TeamDto findOne(Long id) {

        if (id == null) {
            throw new AppException("team service - find one - id is null");
        }

        return teamRepository
                .findById(id)
                .map(ModelMapper::fromTeamToTeamDto)
                .orElseThrow(() -> new AppException("team service - find one - no team with id = " + id));
    }

    public TeamDto add(TeamDto teamDto) {
        if (teamDto == null) {
            throw new AppException("team is null");
        }
        Team team = ModelMapper.fromTeamDtoToTeam(teamDto);
        Team teamFromDb = teamRepository.save(team);
        return ModelMapper.fromTeamToTeamDto(teamFromDb);
    }

    public TeamDto update(Long id, TeamDto teamDto) {
        if (teamDto == null) {
            throw new AppException("team is null");
        }
        Team team = teamRepository.getOne(id);
        team.setName(teamDto.getName() == null ? team.getName() : teamDto.getName());
        team.setPoint(teamDto.getPoint() == null ? team.getPoint() : teamDto.getPoint());
        team.setRate(teamDto.getRate() == null ? team.getRate() : teamDto.getRate());
        Team teamToDB = teamRepository.save(team);
        return ModelMapper.fromTeamToTeamDto(teamToDB);

    }

    public TeamDto delete(Long id){
        if(id == null){
            throw new AppException("team service - find one - id is null");
        }
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new AppException("Team don't exist"));

        playerRepository.saveAll(playerRepository
                .findAllByTeam_Id(team.getId())
                .stream()
                .peek(player -> player.setTeam(null))
                .collect(Collectors.toList()));

        teamRepository.delete(team);
        return ModelMapper.fromTeamToTeamDto(team);

    }

}