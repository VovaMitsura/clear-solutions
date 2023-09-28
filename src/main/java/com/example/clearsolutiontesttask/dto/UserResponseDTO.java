package com.example.clearsolutiontesttask.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserResponseDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate birthDate;
  private String address;
  private String phoneNumber;

}
