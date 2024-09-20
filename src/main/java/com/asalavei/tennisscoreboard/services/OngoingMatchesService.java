package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.dbaccess.mapper.PlayerEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.HibernatePlayerRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerRepository;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    private static final ConcurrentHashMap<UUID, Match> ongoingMatches = new ConcurrentHashMap<>();

    private final PlayerRepository playerRepository = new HibernatePlayerRepository();

    private final PlayerEntityMapper mapper = Mappers.getMapper(PlayerEntityMapper.class);

    public UUID create(Player firstPlayerToPlay, Player secondPlayerToPlay) {
        Optional<PlayerEntity> firstPlayerFound = playerRepository.findByName(firstPlayerToPlay.getName());
        Optional<PlayerEntity> secondPlayerFound = playerRepository.findByName(secondPlayerToPlay.getName());

        PlayerEntity firstPlayerEntity = firstPlayerFound.orElseGet(() -> playerRepository.save(mapper.toEntity(firstPlayerToPlay)));
        PlayerEntity secondPlayerEntity = secondPlayerFound.orElseGet(() -> playerRepository.save(mapper.toEntity(secondPlayerToPlay)));

        Player firstPlayer = mapper.toDto(firstPlayerEntity);
        Player secondPlayer = mapper.toDto(secondPlayerEntity);

        firstPlayer.setGamePoints("0");
        secondPlayer.setGamePoints("0");

        UUID uuid = UUID.randomUUID();

        ongoingMatches.put(uuid, Match.builder()
                .uuid(uuid)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build());

        return uuid;
    }

    public Match getOngoingMatch(UUID uuid) {
        return ongoingMatches.get(uuid);
    }

    public void removeMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}