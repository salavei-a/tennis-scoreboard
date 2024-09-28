package com.asalavei.tennisscoreboard.validation.annotation;

import com.asalavei.tennisscoreboard.validation.validator.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String message() default "Name is incorrect. Use Latin characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
