package com.example.myapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CapitalLetterValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CapitalLetter {
    String value() default "";

    String message() default "Name should start with capital letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}