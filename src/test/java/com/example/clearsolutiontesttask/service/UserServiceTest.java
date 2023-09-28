package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.exception.ApplicationExceptionHandler;
import com.example.clearsolutiontesttask.exception.NotFoundException;
import com.example.clearsolutiontesttask.exception.ParametersException;
import com.example.clearsolutiontesttask.exception.ResourceConflictException;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import com.example.clearsolutiontesttask.util.EntityFactory;
import com.example.clearsolutiontesttask.util.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
class UserServiceTest {

  @Autowired
  UserService userService;

  @MockBean
  UserRepository userRepository;

  EntityFactory<User> entityFactory = new UserFactory();
  List<User> users;

  @BeforeEach
  void setUp() {
    users = entityFactory.createEntityList();
  }

  @Test
  void save_user_should_add_to_db() {
    var user = entityFactory.createEntity("john", "doe", 1);
    users.add(user);
    Mockito.when(userRepository.save(user))
            .thenReturn(users.get(users.size() - 1));

    var response = userService.save(user);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(user.getEmail(), response.getEmail());
    Assertions.assertEquals(user.getFirstName(), response.getFirstName());
    Assertions.assertEquals(user.getLastName(), response.getLastName());
    Assertions.assertEquals(user.getBirthDate(), response.getBirthDate());
    Assertions.assertEquals(user.getPhoneNumber(), response.getPhoneNumber());
    Assertions.assertEquals(user.getAddress(), response.getAddress());
  }

  @Test
  void save_user_should_throw_exception_when_duplicate() {
    var user = entityFactory.createEntity("john", "doe", 1);
    users.add(user);
    Mockito.when(userRepository.save(user))
            .thenThrow(new ResourceConflictException(
                    String.format("User with email %s already exists", user.getEmail()),
                    ApplicationExceptionHandler.DUPLICATE_ENTRY));

    var response = Assertions.assertThrows(ResourceConflictException.class, () -> userService.save(user));
    Assertions.assertEquals(String.format("User with email %s already exists", user.getEmail()), response.getMessage());
    Assertions.assertEquals(ApplicationExceptionHandler.DUPLICATE_ENTRY, response.getErrorCode());
  }

  @Test
  void get_by_id_should_return_user() {
    var curUser = users.get(0);

    Mockito.when(userRepository.findById(curUser.getId()))
            .thenReturn(Optional.of(users.get(0)));

    var response = userService.getById(curUser.getId());

    Assertions.assertNotNull(response);
    Assertions.assertEquals(curUser.getId(), response.getId());
  }

  @Test
  void get_by_id_should_throw_exception_when_id_not_exists() {
    Mockito.when(userRepository.findById(1000L))
            .thenReturn(Optional.empty());

    var response = Assertions.assertThrows(NotFoundException.class, () -> userService.getById(1000L));
    Assertions.assertEquals("User with id 1000 not found", response.getMessage());
    Assertions.assertEquals(ApplicationExceptionHandler.USER_NOT_FOUND, response.getErrorCode());
  }

  @Test
  void remove_user_should_delete_from_db() {
    var curUser = users.get(0);

    Mockito.when(userRepository.findById(curUser.getId()))
            .thenReturn(Optional.of(curUser));

    userService.delete(curUser.getId());

    Mockito.verify(userRepository, Mockito.times(1)).delete(curUser);
  }

  @Test
  void get_all_by_range_should_return_list() {
    var from = users.get(1).getBirthDate();
    var to = users.get(0).getBirthDate();

    Mockito.when(userRepository.findAllByBirthDateBetween(from, to))
            .thenReturn(List.of(users.get(0), users.get(1)));

    var response = userService.getAllByBirthDateBetween(from, to);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(2, response.size());
  }

  @Test
  void get_all_by_range_should_throw_exception_when_range_invalid() {
    var from = users.get(0).getBirthDate();
    var to = users.get(1).getBirthDate();

    var response = Assertions.assertThrows(ParametersException.class,
            () -> userService.getAllByBirthDateBetween(from, to));
    Assertions.assertEquals("`to` date should be after `from` date", response.getMessage());
    Assertions.assertEquals(ApplicationExceptionHandler.INVALID_PARAMETER, response.getErrorCode());
  }

  @Test
  void update_should_update_all_set_fields() {
    var curUser = users.get(9);
    var user = entityFactory.createEntity("john", "doe", 1);

    Mockito.when(userRepository.findById(curUser.getId()))
            .thenReturn(Optional.of(curUser));

    var response = userService.update(curUser.getId(), user);
    Assertions.assertEquals(response, curUser);
  }

}