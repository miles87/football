package com.michonski.football.controller;

import com.michonski.football.dto.TeamDto;
import com.michonski.football.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    ResponseEntity<List<TeamDto>> findAll(){
       return new ResponseEntity<>(teamService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<TeamDto> findOne(@PathVariable Long id){
        return new ResponseEntity<>(teamService.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<TeamDto> add(@RequestBody TeamDto teamDto){
        return new ResponseEntity<>(teamService.add(teamDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<TeamDto> update(@PathVariable Long id,@RequestBody TeamDto teamDto){
        return new ResponseEntity<>(teamService.update(id,teamDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<TeamDto> delete(@PathVariable Long id){
        return new ResponseEntity<>(teamService.delete(id), HttpStatus.OK);
    }


}
