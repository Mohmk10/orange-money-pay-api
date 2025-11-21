package com.orangemoney.api.validation.validator;

import com.orangemoney.api.validation.annotation.MatchingFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class MatchingFieldsValidator implements ConstraintValidator<MatchingFields, Object> {

    private String field;
    private String matchingField;

    @Override
    public void initialize(MatchingFields constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.matchingField = constraintAnnotation.matchingField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Object fieldValue = getFieldValue(obj, field);
            Object matchingFieldValue = getFieldValue(obj, matchingField);

            if (fieldValue == null && matchingFieldValue == null) {
                return true;
            }

            return fieldValue != null && fieldValue.equals(matchingFieldValue);

        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}