package com.example.clearsolutiontesttask.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidationImp.class)
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

  String message() default "phone number should contains `+` sign followed by 12 digits";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
