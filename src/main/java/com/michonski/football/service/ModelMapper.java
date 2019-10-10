package com.michonski.football.service;

import com.michonski.football.dto.PlayerDto;
import com.michonski.football.dto.TeamDto;
import com.michonski.football.dto.security.RegisterUser;
import com.michonski.football.model.Player;
import com.michonski.football.model.Team;
import com.michonski.football.model.security.Role;
import com.michonski.football.model.security.User;

import java.util.HashSet;

public interface ModelMapper {

    static TeamDto fromTeamToTeamDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .point(team.getPoint())
                .rate(team.getRate())
                .build();

    }

    static Team fromTeamDtoToTeam(TeamDto teamDto){
        return Team.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .point(teamDto.getPoint())
                .rate(teamDto.getRate())
                .players(new HashSet<>())
                .build();
    }

    static PlayerDto fromPlayerToPlayerDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .birthDate(player.getBirthDate())
                .number(player.getNumber())
                .price(player.getPrice())
                .teamDto(player.getTeam() == null ? null : fromTeamToTeamDto(player.getTeam()))
                .build();
    }

    static Player fromPlayerDtoToPlayer(PlayerDto playerDto){
        return Player.builder()
                .id(playerDto.getId())
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .birthDate(playerDto.getBirthDate())
                .number(playerDto.getNumber())
                .price(playerDto.getPrice())
                .team(playerDto.getTeamDto() == null ? null : fromTeamDtoToTeam(playerDto.getTeamDto()))
                .build();
    }

    static User fromRegisterUserToUser(RegisterUser registerUser) {
        return registerUser == null ? null : User.builder()
                .username(registerUser.getUsername())
                .password(registerUser.getPassword())
                .role(Role.ROLE_USER)
                .build();
    }

}
