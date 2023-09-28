package com.example.clearsolutiontesttask.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PhoneNumberValidationImp implements ConstraintValidator<PhoneNumber, String> {

  private static final String PHONE_NUMBER_REGEX = "\\+\\d{12}";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if(Objects.isNull(value)){
      return true;
    }

    return value.matches(PHONE_NUMBER_REGEX);
  }
}
