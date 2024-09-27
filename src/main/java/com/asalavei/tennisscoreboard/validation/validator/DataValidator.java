package com.asalavei.tennisscoreboard.validation.validator;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.exceptions.ForbiddenException;
import com.asalavei.tennisscoreboard.web.dto.MatchRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
public class DataValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DataValidator() {
    }

    public static void validatePointWinner(UUID pointWinnerUuid, Match match) {
        if (isPlayerNotInMatch(pointWinnerUuid, match)) {
            throw new ForbiddenException(String.format("Player with UUID=%s is not participating in match=%s", pointWinnerUuid, match));
        }
    }

    private static boolean isPlayerNotInMatch(UUID pointWinnerUuid, Match match) {
        return !Arrays.asList(
                match.getFirstPlayer().getUuid(),
                match.getSecondPlayer().getUuid()
        ).contains(pointWinnerUuid);
    }

    public static void validateMatch(MatchRequestDto match) {
        validate(match);

        if (arePlayersSame(match)) {
            String message = "First player and second player cannot be the same";
            log.info(String.format("%s. Match details: %s", message, match));
            throw new ValidationException(message);
        }
    }

    private static boolean arePlayersSame(MatchRequestDto match) {
        return match.getFirstPlayer().getName().equals(match.getSecondPlayer().getName());
    }

    public static <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String violationsMessage = getViolationsMessage(violations);
            log.info(String.format("Validation failed for %s. Violations: %s", dto, violationsMessage));
            throw new ValidationException(violationsMessage);
        }
    }

    private static <T> String getViolationsMessage(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    public static UUID getValidatedUuid(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ForbiddenException(String.format("Invalid UUID=%s", uuid));
        }
    }
}
