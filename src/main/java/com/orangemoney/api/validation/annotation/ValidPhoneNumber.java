package com.orangemoney.api.validation.annotation;

import com.orangemoney.api.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {

    String message() default "Numéro de téléphone Orange invalide (doit commencer par 77, 78, 76 ou 70)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}