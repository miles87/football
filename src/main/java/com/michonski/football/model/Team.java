package com.michonski.football.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Teams")
public class Team {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer point;
    private Double rate;


    @OneToMany(mappedBy = "team")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Player> players;


}
