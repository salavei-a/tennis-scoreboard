package com.asalavei.tennisscoreboard.dbaccess.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "first_player_id")
    private PlayerEntity firstPlayer;

    @ManyToOne
    @JoinColumn(name = "second_player_id")
    private PlayerEntity secondPlayer;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private PlayerEntity winner;
}
