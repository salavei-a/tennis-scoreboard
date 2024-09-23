package com.asalavei.tennisscoreboard.validation;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.exceptions.ForbiddenException;
import com.asalavei.tennisscoreboard.exceptions.NotFoundException;
import com.asalavei.tennisscoreboard.web.dto.MatchRequestDto;
import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
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

    private static final String NOT_FOUND = "Not found";
    private static final String FORBIDDEN = "Forbidden";

    private DataValidator() {
    }

    public static void validatePointWinner(PlayerRequestDto pointWinner, Match match, Class<?> group) {
        validate(pointWinner, group);

        Integer pointWinnerId = pointWinner.getId();

        if (isPlayerNotInMatch(pointWinnerId, match)) {
            log.info(String.format("Player with id=%s is not participating in match=%s", pointWinnerId, match));
            throw new ForbiddenException(FORBIDDEN);
        }
    }

    private static boolean isPlayerNotInMatch(Integer pointWinner, Match match) {
        return !Arrays.asList(
                match.getFirstPlayer().getId(),
                match.getSecondPlayer().getId()
        ).contains(pointWinner);
    }

    public static void validateMatch(MatchRequestDto match, Class<?> group) {
        validate(match, group);

        if (arePlayersSame(match)) {
            String message = "First player and second player cannot be the same";
            log.info(String.format("%s: %s", message, match));
            throw new ValidationException(message);
        }
    }

    private static boolean arePlayersSame(MatchRequestDto match) {
        return match.getFirstPlayer().getName().equals(match.getSecondPlayer().getName());
    }

    public static void validateMatch(MatchRequestDto matchRequestDto, Match match, Class<?> group) {
        validate(matchRequestDto, group);

        if (isMatchNotFound(matchRequestDto, match)) {
            log.info(String.format("No current match found with UUID=%s", matchRequestDto.getUuid()));
            throw new NotFoundException(NOT_FOUND);
        }
    }

    private static boolean isMatchNotFound(MatchRequestDto matchRequestDto, Match match) {
        return match.getUuid() == null || !match.getUuid().equals(matchRequestDto.getUuid());
    }

    public static <T> void validate(T dto, Class<?> group) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto, group);

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

    public static Integer getValidatedNumber(String param) {
        try {
            return Integer.valueOf(param);
        } catch (NumberFormatException e) {
            log.info(String.format("Invalid number: %s", param));
            throw new ForbiddenException(FORBIDDEN);
        }
    }

    public static UUID getValidatedUuid(String uuidParam) {
        try {
            return UUID.fromString(uuidParam);
        } catch (IllegalArgumentException e) {
            log.info(String.format("Invalid UUID: %s", uuidParam));
            throw new NotFoundException(NOT_FOUND);
        }
    }
}
