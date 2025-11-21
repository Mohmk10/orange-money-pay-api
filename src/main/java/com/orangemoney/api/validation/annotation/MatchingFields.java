package com.orangemoney.api.validation.annotation;

import com.orangemoney.api.validation.validator.MatchingFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchingFieldsValidator.class)
public @interface MatchingFields {

    String message() default "Les champs ne correspondent pas";

    String field();

    String matchingField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}