package com.example.clearsolutiontesttask.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
class BirthDateValidatorImpTest {

  @Autowired
  BirthDateValidatorImp birthDateValidatorImp;

  public static Stream<Arguments> age_less_min_age() {
    return Stream.of(
            Arguments.of(LocalDate.of(2006, 1, 1)),
            Arguments.of(LocalDate.of(2007, 1, 1))
    );
  }

  @ParameterizedTest
  @MethodSource("age_greater_and_equals_min_age")
  void age_greater_and_equals_min_age_return_true(LocalDate birthDate) {
    var result = birthDateValidatorImp.isValid(birthDate, null);
    Assertions.assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("age_less_min_age")
  void age_less_min_age_return_false(LocalDate birthDate) {
    var result = birthDateValidatorImp.isValid(birthDate, null);
    Assertions.assertFalse(result);
  }

  private static Stream<Arguments> age_greater_and_equals_min_age() {
    return Stream.of(
            Arguments.of(LocalDate.of(2005, 1, 1)),
            Arguments.of(LocalDate.of(2004, 1, 1)),
            Arguments.of(LocalDate.of(2003, 1, 1))
    );
  }


}