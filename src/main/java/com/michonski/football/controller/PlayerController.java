package com.michonski.football.controller;

import com.michonski.football.dto.PlayerDto;
import com.michonski.football.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    ResponseEntity<List<PlayerDto>> getAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<PlayerDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findOne(id));
    }

    @PostMapping
    ResponseEntity<PlayerDto> addPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.add(playerDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<PlayerDto> deletePlayer(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.deletePlayer(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<PlayerDto> updatePlayer(@PathVariable long id, @RequestBody PlayerDto playerDto){
        return ResponseEntity.ok(playerService.updatePlayer(id, playerDto));
    }


}
