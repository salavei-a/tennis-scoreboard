package com.asalavei.tennisscoreboard.dbaccess.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Builder
@Check(constraints = "first_player_id <> second_player_id")
@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "first_player_id", nullable = false)
    private PlayerEntity firstPlayer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "second_player_id", nullable = false)
    private PlayerEntity secondPlayer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "winner_id", nullable = false)
    private PlayerEntity winner;
}
