package com.example.myapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CapitalLetterValidator
        implements ConstraintValidator<CapitalLetter, String> {

    @Override
    public boolean isValid(String courierName,
                           ConstraintValidatorContext constraintValidatorContext) {
        boolean result;
        if (courierName != null) {
            result = Character.isUpperCase(courierName.charAt(0));
            ;
        } else {
            result = true;
        }
        return result;
    }
}