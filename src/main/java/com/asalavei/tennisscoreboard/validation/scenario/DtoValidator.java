package com.asalavei.tennisscoreboard.validation.scenario;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;

import java.util.Set;

public class DtoValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DtoValidator() {
    }

    public static <T> void validate(T dto, Class<?> group, String pagePath) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto, group);

        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage(), pagePath);
        }
    }
}
