package com.example.clearsolutiontesttask.dto;

import com.example.clearsolutiontesttask.validators.BirthDate;
import com.example.clearsolutiontesttask.validators.PhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPatchRequestDTO {
  private String firstName;
  private String lastName;
  @Email(message = "Email should be valid",
          regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
  private String email;
  @BirthDate
  private LocalDate birthDate;
  private String address;
  @PhoneNumber
  private String phoneNumber;
}
