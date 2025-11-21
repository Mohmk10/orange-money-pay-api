package com.orangemoney.api.validation.validator;

import com.orangemoney.api.common.constants.ValidationConstants;
import com.orangemoney.api.validation.annotation.ValidPIN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PINValidator implements ConstraintValidator<ValidPIN, String> {

    @Override
    public boolean isValid(String pin, ConstraintValidatorContext context) {
        if (pin == null || pin.isBlank()) {
            return true;
        }

        if (!pin.matches(ValidationConstants.PIN_REGEX)) {
            return false;
        }

        return Arrays.stream(ValidationConstants.FORBIDDEN_PINS)
                .noneMatch(forbiddenPin -> forbiddenPin.equals(pin));
    }
}