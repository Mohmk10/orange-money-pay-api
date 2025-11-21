package com.orangemoney.api.validation.annotation;

import com.orangemoney.api.validation.validator.PINValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PINValidator.class)
public @interface ValidPIN {

    String message() default "Code PIN invalide (4 chiffres requis, codes simples interdits)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}