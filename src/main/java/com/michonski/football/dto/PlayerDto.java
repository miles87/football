package com.michonski.football.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer number;
    private BigDecimal price;
    private LocalDate birthDate;
    private TeamDto teamDto;
}
