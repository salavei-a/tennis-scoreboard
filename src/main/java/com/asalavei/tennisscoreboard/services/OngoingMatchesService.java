package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.mapper.PlayerEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerHibernateRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.PlayerRepository;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.exceptions.NotFoundException;
import org.mapstruct.factory.Mappers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    private static final ConcurrentHashMap<UUID, Match> ongoingMatches = new ConcurrentHashMap<>();

    private final PlayerRepository playerRepository = new PlayerHibernateRepository();

    private final PlayerEntityMapper mapper = Mappers.getMapper(PlayerEntityMapper.class);

    public UUID create(Match match) {
        Player firstPlayerPersisted = getOrCreatePlayer(match.getFirstPlayer());
        Player secondPlayerPersisted = getOrCreatePlayer(match.getSecondPlayer());

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

    public Match getOngoingMatch(UUID uuid) {
        Match match = ongoingMatches.get(uuid);

        if (match == null) {
            throw new NotFoundException(String.format("No current match found with UUID=%s", uuid));
        }

        return match;
    }

    public void removeMatch(UUID uuid) {
        ongoingMatches.remove(uuid);
    }
}