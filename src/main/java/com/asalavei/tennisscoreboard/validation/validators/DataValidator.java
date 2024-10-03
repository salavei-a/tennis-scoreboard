package com.asalavei.tennisscoreboard.validation.validators;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.enums.PlayerNumber;
import com.asalavei.tennisscoreboard.web.dto.MatchRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import lombok.extern.java.Log;

import java.util.Set;
import java.util.stream.Collectors;

@Log
public class DataValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DataValidator() {
    }

    public static void validatePlayerParticipation(int playerNumber, Match match) {
        if (isPlayerNotParticipant(playerNumber)) {
            throw new ValidationException(String.format("Player with number=%s is not participating in match=%s", playerNumber, match));
        }
    }

    private static boolean isPlayerNotParticipant(int playerNumber) {
        return playerNumber != PlayerNumber.FIRST_PLAYER.getNumber() &&
                playerNumber != PlayerNumber.SECOND_PLAYER.getNumber();
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
        validate(dto, null);
    }

    public static <T> void validate(T dto, Class<?> group) {
        Set<ConstraintViolation<T>> violations = (group == null) ?
                validator.validate(dto) :
                validator.validate(dto, group);

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
}
