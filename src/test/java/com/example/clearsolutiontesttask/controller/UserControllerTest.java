package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.dto.UserPatchRequestDTO;
import com.example.clearsolutiontesttask.dto.UserRequestDTO;
import com.example.clearsolutiontesttask.dto.UserResponseDTO;
import com.example.clearsolutiontesttask.exception.ApplicationExceptionHandler;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import com.example.clearsolutiontesttask.service.UserService;
import com.example.clearsolutiontesttask.util.EntityFactory;
import com.example.clearsolutiontesttask.util.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
class UserControllerTest {

  final String url = "/api/v1/users";

  @Autowired
  WebApplicationContext applicationContext;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserService userService;

  MockMvc mockMvc;
  EntityFactory<User> userEntityFactory = new UserFactory();

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .build();
  }

  @Test
  void should_save_user_to_db() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    var request = new UserRequestDTO(curUser.getEmail(),
            curUser.getFirstName(),
            curUser.getLastName(),
            curUser.getBirthDate(),
            curUser.getAddress(),
            curUser.getPhoneNumber());

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            UserResponseDTO.class);

    Assertions.assertNotNull(response.getId());
    Assertions.assertEquals(response.getFirstName(), curUser.getFirstName());
    Assertions.assertEquals(response.getLastName(), curUser.getLastName());
    Assertions.assertEquals(response.getEmail(), curUser.getEmail());
    Assertions.assertEquals(response.getBirthDate(), curUser.getBirthDate());
    Assertions.assertEquals(response.getAddress(), curUser.getAddress());
    Assertions.assertEquals(response.getPhoneNumber(), curUser.getPhoneNumber());

    userRepository.deleteById(response.getId());
  }

  @Test
  void save_should_throw_exception_when_user_exists() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    var request = new UserRequestDTO(curUser.getEmail(),
            curUser.getFirstName(),
            curUser.getLastName(),
            curUser.getBirthDate(),
            curUser.getAddress(),
            curUser.getPhoneNumber());

    userService.save(curUser);

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            ApplicationExceptionHandler.ErrorResponse.class);

    Assertions.assertNotNull(response);
    Assertions.assertEquals("409 CONFLICT", response.getStatus());
    Assertions.assertEquals(ApplicationExceptionHandler.DUPLICATE_ENTRY, response.getDescription());
    Assertions.assertEquals(String.format("User with email %s already exists", curUser.getEmail()),
            response.getDetails());

    userRepository.deleteById(curUser.getId());
  }

  @Test
  void get_all_by_range_should_return_list() throws Exception {
    var curUser1 = userEntityFactory.createEntity("john", "doe", 1);
    var curUser2 = userEntityFactory.createEntity("dan", "smith", 2);

    userRepository.save(curUser1);
    userRepository.save(curUser2);

    var from = curUser2.getBirthDate();
    var to = curUser1.getBirthDate();

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .param("from", from.toString())
                    .param("to", to.toString()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var response = List.of(objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            UserResponseDTO[].class));

    Assertions.assertEquals(2, response.size());
    Assertions.assertEquals(curUser1.getFirstName(), response.get(0).getFirstName());
    Assertions.assertEquals(curUser2.getFirstName(), response.get(1).getFirstName());

    userRepository.deleteById(curUser1.getId());
    userRepository.deleteById(curUser2.getId());
  }

  @Test
  void get_all_by_range_should_return_no_content() throws Exception {
    var from = "2021-01-01";
    var to = "2021-01-02";

    this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .param("from", from)
                    .param("to", to))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  void get_all_by_range_should_throw_exception_when_range_invalid() throws Exception {
    var from = "2021-01-02";
    var to = "2021-01-01";

    var response = this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                    .param("from", from)
                    .param("to", to))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var errorResponse = objectMapper.readValue(response.getResponse().getContentAsString(),
            ApplicationExceptionHandler.ErrorResponse.class);

    Assertions.assertNotNull(errorResponse);
    Assertions.assertEquals("400 BAD_REQUEST", errorResponse.getStatus());
    Assertions.assertEquals(ApplicationExceptionHandler.INVALID_PARAMETER, errorResponse.getDescription());
    Assertions.assertEquals("`to` date should be after `from` date", errorResponse.getDetails());
  }

  @Test
  void should_full_update_user() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    var request = new UserRequestDTO("dansmith@mmail.com",
            "dan",
            "smith",
            curUser.getBirthDate(),
            curUser.getAddress(),
            curUser.getPhoneNumber());

    curUser = userRepository.save(curUser);

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put(url + "/" + curUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            UserResponseDTO.class);

    Assertions.assertEquals(response.getFirstName(), request.getFirstName());
    Assertions.assertEquals(response.getLastName(), request.getLastName());
    Assertions.assertEquals(response.getEmail(), request.getEmail());
    Assertions.assertEquals(curUser.getBirthDate(), request.getBirthDate());

    userRepository.deleteById(curUser.getId());
  }

  @Test
  void partial_update_should_update_fields() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    var request = UserPatchRequestDTO.builder()
            .lastName("lennon")
            .email("johnlennon@mmail.com")
            .phoneNumber("+380501234567")
            .build();

    curUser = userRepository.save(curUser);

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.patch(url + "/" + curUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), UserResponseDTO.class);

    Assertions.assertEquals(response.getLastName(), request.getLastName());
    Assertions.assertEquals(response.getEmail(), request.getEmail());
    Assertions.assertEquals(response.getPhoneNumber(), request.getPhoneNumber());
    Assertions.assertEquals(curUser.getFirstName(), response.getFirstName());

    userRepository.deleteById(curUser.getId());
  }

  @Test
  void partial_update_with_invalid_field_throw_bad_request() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    var request = UserPatchRequestDTO.builder()
            .lastName("lennon")
            .email("lenonmail.com")
            .phoneNumber("+3805012345671111")
            .build();

    curUser = userRepository.save(curUser);

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.patch(url + "/" + curUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            ApplicationExceptionHandler.ErrorResponse.class);

    Assertions.assertNotNull(response);
    Assertions.assertEquals("400 BAD_REQUEST", response.getStatus());
    Assertions.assertEquals(ApplicationExceptionHandler.BAD_REQUEST, response.getDescription());
    Assertions.assertEquals("{phoneNumber=phone number should contains `+` sign followed by 12 digits, " +
            "email=Email should be valid}",
            response.getDetails());

    userRepository.deleteById(curUser.getId());
  }

  @Test
  void delete_user_by_id_should_remove_from_db() throws Exception {
    var curUser = userEntityFactory.createEntity("john", "doe", 1);
    curUser = userRepository.save(curUser);

    this.mockMvc.perform(MockMvcRequestBuilders.delete(url + "/" + curUser.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

    Assertions.assertFalse(userRepository.existsById(curUser.getId()));

    userRepository.deleteById(curUser.getId());
  }

  @Test
  void delete_by_invalid_id_should_throw_not_found() throws Exception {

    var mockMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(url + "/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

    var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
            ApplicationExceptionHandler.ErrorResponse.class);

    Assertions.assertNotNull(response.getTimestamp());
    Assertions.assertEquals("404 NOT_FOUND", response.getStatus());
    Assertions.assertEquals(ApplicationExceptionHandler.USER_NOT_FOUND, response.getDescription());
    Assertions.assertEquals("User with id 1 not found", response.getDetails());
  }
}