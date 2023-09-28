package com.example.clearsolutiontesttask.util;

import com.example.clearsolutiontesttask.model.User;
import org.hibernate.query.sqm.TemporalUnit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFactory implements EntityFactory<User> {
  @Override
  public User createEntity(String firstName, String lastName, int years) {
    return User.builder()
            .id(UUID.randomUUID().getMostSignificantBits())
            .firstName(firstName)
            .lastName(lastName)
            .email(firstName + lastName + "@mmail.com")
            .address("Some address")
            .phoneNumber("+38050123456" + (int) ((Math.random() * 5) + 1))
            .birthDate(LocalDate.of(1990, 1, 1).minusYears(years))
            .build();
  }

  @Override
  public List<User> createEntityList() {
    var result = new ArrayList<User>();
    for (int i = 0; i < 10; i++) {
      result.add(createEntity("firstName" + i, "lastName" + i, i));
    }
    return result;
  }

}
