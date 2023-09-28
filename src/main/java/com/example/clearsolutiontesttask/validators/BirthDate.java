package com.example.clearsolutiontesttask.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = BirthDateValidatorImp.class)
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface BirthDate {

  String message() default "birth date should be in the past and user should be older than 18 years";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
