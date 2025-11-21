package com.orangemoney.api.validation.validator;

import com.orangemoney.api.common.constants.ValidationConstants;
import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        return phoneNumber.matches(ValidationConstants.PHONE_NUMBER_REGEX);
    }
}