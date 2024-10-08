package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.dbaccess.mapper.PlayerEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerHibernateRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerRepository;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.dto.PlayerScore;
import com.asalavei.tennisscoreboard.enums.GameScore;
import org.mapstruct.factory.Mappers;

public class PlayerService {

    private final PlayerRepository playerRepository = new PlayerHibernateRepository();

    private final PlayerEntityMapper mapper = Mappers.getMapper(PlayerEntityMapper.class);

    public Player findOrSave(Player player) {
        return playerRepository.findByName(player.getName())
                .map(this::buildPlayer)
                .orElseGet(() -> buildPlayer(playerRepository.save(mapper.toEntity(player))));
    }

    private Player buildPlayer(PlayerEntity entity) {
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .playerScore(
                        PlayerScore.builder()
                                .gameScore(GameScore.LOVE)
                                .build()
                )
                .build();
    }
}
