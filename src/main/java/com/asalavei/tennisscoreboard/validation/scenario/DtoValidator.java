package com.asalavei.tennisscoreboard.validation.scenario;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Log
public class DtoValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DtoValidator() {
    }

    public static void validatePointWinner(PlayerRequestDto pointWinner, Match match, Class<?> group) {
        validate(pointWinner, group, null);

        Integer pointWinnerId = pointWinner.getId();

        if (!isPlayerInMatch(pointWinnerId, match)) {
            log.info(String.format("Validation error: Player with id=%s is not participating in match=%s",
                    pointWinnerId, match));
            throw new ValidationException("Player is not participating in this match");
        }
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

    private static boolean isPlayerInMatch(Integer pointWinner, Match match) {
        return Arrays.asList(
                match.getFirstPlayer().getId(),
                match.getSecondPlayer().getId()
        ).contains(pointWinner);
    }

    public static Integer getValidatedNumber(String param) {
        try {
            return Integer.valueOf(param);
        } catch (NumberFormatException e) {
            log.info(String.format("Validation error: '%s' is not a valid number", param));
            throw new ValidationException("An error occurred. Please try again");
        }
    }
}
