package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.exception.ApplicationExceptionHandler;
import com.example.clearsolutiontesttask.exception.NotFoundException;
import com.example.clearsolutiontesttask.exception.ParametersException;
import com.example.clearsolutiontesttask.exception.ResourceConflictException;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User save(User user) {
    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ResourceConflictException(String.format("User with email %s already exists", user.getEmail()),
              ApplicationExceptionHandler.DUPLICATE_ENTRY);
    }
  }

  public List<User> getAllByBirthDateBetween(LocalDate from, LocalDate to) {
    if (to.isBefore(from)) {
      throw new ParametersException("`to` date should be after `from` date", ApplicationExceptionHandler.INVALID_PARAMETER);
    }
    return userRepository.findAllByBirthDateBetween(from, to);
  }


  public User getById(Long usedId) {
    return userRepository.findById(usedId)
            .orElseThrow(() ->
                    new NotFoundException(String.format("User with id %s not found", usedId),
                            ApplicationExceptionHandler.USER_NOT_FOUND));
  }

  public void delete(Long id) {
    var uToDelete = getById(id);
    userRepository.delete(uToDelete);
  }

  @Transactional
  public User update(Long id, User user) {
    var userToUpdate = getById(id);

    if (Objects.nonNull(user.getFirstName()))
      userToUpdate.setFirstName(user.getFirstName());
    if (Objects.nonNull(user.getLastName()))
      userToUpdate.setLastName(user.getLastName());
    if (Objects.nonNull(user.getBirthDate()))
      userToUpdate.setBirthDate(user.getBirthDate());
    if (Objects.nonNull(user.getAddress()))
      userToUpdate.setAddress(user.getAddress());
    if (Objects.nonNull(user.getPhoneNumber()))
      userToUpdate.setPhoneNumber(user.getPhoneNumber());
    if (Objects.nonNull(user.getEmail()))
      userToUpdate.setEmail(user.getEmail());

    return userToUpdate;
  }
}
