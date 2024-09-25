package com.asalavei.tennisscoreboard.validation.validator;

import com.asalavei.tennisscoreboard.validation.annotation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String NAME_PATTERN = "^[A-Za-z]+([\\s'\\-][A-Za-z]+)*+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        return value.matches(NAME_PATTERN);
    }
}
