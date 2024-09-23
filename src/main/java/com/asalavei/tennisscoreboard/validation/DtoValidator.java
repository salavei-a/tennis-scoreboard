package com.asalavei.tennisscoreboard.validation;

import com.asalavei.tennisscoreboard.dto.Match;
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
public class DtoValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ERROR_OCCURRED = "An error occurred";
    private static final String INVALID_URL = "Invalid URL";

    private DtoValidator() {
    }

    public static void validatePointWinner(PlayerRequestDto pointWinner, Match match, Class<?> group) {
        validate(pointWinner, group, null);

        Integer pointWinnerId = pointWinner.getId();

        if (isPlayerNotInMatch(pointWinnerId, match)) {
            log.info(String.format("Validation error: Player with id=%s is not participating in match=%s",
                    pointWinnerId, match));
            throw new ValidationException(ERROR_OCCURRED);
        }
    }

    private static boolean isPlayerNotInMatch(Integer pointWinner, Match match) {
        return !Arrays.asList(
                match.getFirstPlayer().getId(),
                match.getSecondPlayer().getId()
        ).contains(pointWinner);
    }

    public static void validateMatch(MatchRequestDto match, Class<?> group, String pagePath) {
        validate(match, group, pagePath);

        if (arePlayersSame(match)) {
            log.info(String.format("Validation error: First player and second player cannot be the same. %s", match));
            throw new ValidationException("First player and second player cannot be the same", pagePath);
        }
    }

    private static boolean arePlayersSame(MatchRequestDto match) {
        return match.getFirstPlayer().getName().equals(match.getSecondPlayer().getName());
    }

    public static void validateMatch(MatchRequestDto matchRequestDto, Match match, Class<?> group, String pagePath) {
        validate(matchRequestDto, group, pagePath);

        if (isMatchNotFound(matchRequestDto, match)) {
            log.info(String.format("Validation error: No current match found with UUID=%s", matchRequestDto.getUuid()));
            throw new ValidationException(INVALID_URL, pagePath);
        }
    }

    private static boolean isMatchNotFound(MatchRequestDto matchRequestDto, Match match) {
        return match.getUuid() == null || !match.getUuid().equals(matchRequestDto.getUuid());
    }

    public static <T> void validate(T dto, Class<?> group, String pagePath) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto, group);

        if (!violations.isEmpty()) {
            String violationsMessage = getViolationsMessage(violations);

            log.info(String.format("Validation error: %s. %s", violationsMessage, dto));
            throw new ValidationException(violationsMessage, pagePath);
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
            log.info(String.format("Validation error: '%s' is not a valid number", param));
            throw new ValidationException(ERROR_OCCURRED);
        }
    }

    public static UUID getValidatedUuid(String uuidParam, String pagePath) {
        try {
            return UUID.fromString(uuidParam);
        } catch (IllegalArgumentException e) {
            log.info(String.format("Validation error: '%s' is not a valid UUID", uuidParam));
            throw new ValidationException(INVALID_URL, pagePath);
        }
    }
}
