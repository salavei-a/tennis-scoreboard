package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.exceptions.NotFoundException;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    private static final ConcurrentHashMap<UUID, Match> ongoingMatches = new ConcurrentHashMap<>();

    private final PlayerService playerService = new PlayerService();

    public UUID create(Match match) {
        Player firstPlayer = playerService.findOrSave(match.getFirstPlayer());
        Player secondPlayer = playerService.findOrSave(match.getSecondPlayer());

        UUID uuid = UUID.randomUUID();

        ongoingMatches.put(uuid, Match.builder()
                .uuid(uuid)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build());

        return uuid;
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