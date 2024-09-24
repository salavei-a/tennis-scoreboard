package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.mapper.PlayerEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerHibernateRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerRepository;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    private static final ConcurrentHashMap<UUID, Match> ongoingMatches = new ConcurrentHashMap<>();

    private final PlayerRepository playerRepository = new PlayerHibernateRepository();

    private final PlayerEntityMapper mapper = Mappers.getMapper(PlayerEntityMapper.class);

    public UUID create(Player firstPlayer, Player secondPlayer) {
        Player firstPlayerPersisted = getOrCreatePlayer(firstPlayer);
        Player secondPlayerPersisted = getOrCreatePlayer(secondPlayer);

        firstPlayerPersisted.setGamePoints("0");
        secondPlayerPersisted.setGamePoints("0");

        UUID uuid = UUID.randomUUID();

        ongoingMatches.put(uuid, Match.builder()
                .uuid(uuid)
                .firstPlayer(firstPlayerPersisted)
                .secondPlayer(secondPlayerPersisted)
                .build());

        return uuid;
    }

    private Player getOrCreatePlayer(Player player) {
        return playerRepository.findByName(player.getName())
                .map(mapper::toDto)
                .orElseGet(() -> mapper.toDto(playerRepository.save(mapper.toEntity(player)))
                );
    }

    public Optional<Match> getOngoingMatch(UUID uuid) {
        return Optional.ofNullable(ongoingMatches.get(uuid));
    }

    public void removeMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}