package com.example.clearsolutiontesttask.util;

import java.util.List;

public interface EntityFactory <T>{
  T createEntity(String firstName, String lastName, int minusYears);
  List<T> createEntityList();
}
