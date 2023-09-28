package com.example.clearsolutiontesttask.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
class PhoneNumberValidationImpTest {

  @Autowired
  PhoneNumberValidationImp phoneNumberValidationImp;

  @ParameterizedTest
  @MethodSource("valid_phone_number")
  void valid_phone_number_should_return_true(String phoneNumber) {
    var result = phoneNumberValidationImp.isValid(phoneNumber, null);
    Assertions.assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("invalid_phone_number")
  void invalid_phone_number_should_return_false(String phoneNumber) {
    var result = phoneNumberValidationImp.isValid(phoneNumber, null);
    Assertions.assertFalse(result);
  }


  public static Stream<Arguments> valid_phone_number() {
    return Stream.of(
            Arguments.of("+380501234567"),
            Arguments.of("+380501234567")
    );
  }


  public static Stream<Arguments> invalid_phone_number() {
    return Stream.of(
            Arguments.of("380501234567"),
            Arguments.of("+38050123456"),
            Arguments.of("+3805012345678")
    );
  }

}