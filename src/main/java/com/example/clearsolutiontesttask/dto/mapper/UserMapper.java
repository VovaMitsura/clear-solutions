package com.example.clearsolutiontesttask.dto.mapper;

import com.example.clearsolutiontesttask.dto.UserPatchRequestDTO;
import com.example.clearsolutiontesttask.dto.UserRequestDTO;
import com.example.clearsolutiontesttask.dto.UserResponseDTO;
import com.example.clearsolutiontesttask.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

  public User toModel(UserRequestDTO request) {
    User user = new User();
    BeanUtils.copyProperties(request, user);
    return user;
  }

  public User toModel(UserPatchRequestDTO patchRequest) {
    var user = new UserRequestDTO();
    if (!Objects.isNull(patchRequest.getFirstName())
            && !patchRequest.getFirstName().isBlank()) {
      user.setFirstName(patchRequest.getFirstName());
    }

    if (!Objects.isNull(patchRequest.getLastName())
            && !patchRequest.getLastName().isBlank()) {
      user.setLastName(patchRequest.getLastName());
    }

    if (!Objects.isNull(patchRequest.getEmail())
            && !patchRequest.getEmail().isBlank()) {
      user.setEmail(patchRequest.getEmail());
    }

    if (!Objects.isNull(patchRequest.getBirthDate())) {
      user.setBirthDate(patchRequest.getBirthDate());
    }

    if (!Objects.isNull(patchRequest.getAddress())
            && !patchRequest.getAddress().isBlank()) {
      user.setAddress(patchRequest.getAddress());
    }

    if (!Objects.isNull(patchRequest.getPhoneNumber())
            && !patchRequest.getPhoneNumber().isBlank()) {
      user.setPhoneNumber(patchRequest.getPhoneNumber());
    }

    return toModel(user);
  }

  public UserResponseDTO toResponseDTO(User user) {
    UserResponseDTO response = new UserResponseDTO();
    BeanUtils.copyProperties(user, response);
    return response;
  }
}
