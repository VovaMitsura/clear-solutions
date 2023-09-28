package com.example.clearsolutiontesttask.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {

  public static final String USER_NOT_FOUND = "user_not_found";
  public static final String DUPLICATE_ENTRY = "duplicate_entry";
  public static final String BAD_REQUEST = "bad_request";
  public static final String INVALID_PARAMETER = "invalid_parameter";

  @ResponseBody
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorResponse handleNotFoundException(NotFoundException ex) {
    return new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.toString(), ex.getErrorCode(), ex.getMessage());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ResourceConflictException.class)
  public ErrorResponse handleResourceConflictException(ResourceConflictException ex) {
    return new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.toString(), ex.getErrorCode(), ex.getMessage());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ParametersException.class)
  public ErrorResponse handleParametersException(ParametersException ex) {
    return new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.toString(), ex.getErrorCode(), ex.getMessage());
  }


  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    var details = getErrorMap(ex);

    return ResponseEntity.badRequest().
            body(new ErrorResponse(LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.toString(),
                    BAD_REQUEST,
                    details.toString()));
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class ErrorResponse {

    private LocalDateTime timestamp;
    private String status;
    private String description;
    private String details;
  }

  private Map<String, String> getErrorMap(MethodArgumentNotValidException ex) {
    var result = new HashMap<String, String>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      result.put(error.getField(), error.getDefaultMessage());
    }
    return result;
  }
}
