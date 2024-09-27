package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.dbaccess.mapper.PlayerEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerHibernateRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerRepository;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

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
                .uuid(UUID.randomUUID())
                .id(entity.getId())
                .name(entity.getName())
                .gamePoints("0")
                .build();
    }
}
