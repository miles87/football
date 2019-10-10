package com.michonski.football.repository;

import com.michonski.football.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByTeam_Id(Long id);
}
