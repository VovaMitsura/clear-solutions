package com.example.clearsolutiontesttask.dto;

import com.example.clearsolutiontesttask.validators.BirthDate;
import com.example.clearsolutiontesttask.validators.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
  @Email(message = "Email should be valid",
          regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
  private String email;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @BirthDate
  private LocalDate birthDate;
  private String address;
  @PhoneNumber
  private String phoneNumber;
}
