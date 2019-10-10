package com.michonski.football.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Players")
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private Integer number;
    private BigDecimal price;
    private LocalDate birthDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;


}
