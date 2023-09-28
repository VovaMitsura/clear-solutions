package com.example.clearsolutiontesttask.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
@Getter
public class BirthDateValidatorImp implements ConstraintValidator<BirthDate, LocalDate> {

  @Value("${min-age}")
  private int minAge;

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
    if (Objects.isNull(birthDate)) return true;
    var age = LocalDate.from(birthDate).until(LocalDate.now()).getYears();
    return age >= minAge;
  }
}
