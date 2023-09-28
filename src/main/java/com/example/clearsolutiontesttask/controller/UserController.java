package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.dto.UserPatchRequestDTO;
import com.example.clearsolutiontesttask.dto.UserRequestDTO;
import com.example.clearsolutiontesttask.dto.UserResponseDTO;
import com.example.clearsolutiontesttask.dto.mapper.UserMapper;
import com.example.clearsolutiontesttask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDTO save(@RequestBody @Valid UserRequestDTO request) {
    return userMapper.toResponseDTO(userService.save(userMapper.toModel(request)));
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> getAllByBirthDateBetween(
          @RequestParam(name = "from")
          @DateTimeFormat LocalDate from,
          @RequestParam(name = "to") @DateTimeFormat LocalDate to) {
    var users = userService.getAllByBirthDateBetween(from, to);
    var userResponse = users.stream().map(userMapper::toResponseDTO).toList();

    if (userResponse.isEmpty())
      return ResponseEntity.noContent().build();

    return ResponseEntity.ok().body(userResponse);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDTO patchUpdate(@PathVariable Long id, @RequestBody @Valid UserPatchRequestDTO request) {
    return userMapper.toResponseDTO(userService.update(id, userMapper.toModel(request)));
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDTO putUpdate(@PathVariable Long id, @RequestBody @Valid UserRequestDTO request) {
    return userMapper.toResponseDTO(userService.update(id, userMapper.toModel(request)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
