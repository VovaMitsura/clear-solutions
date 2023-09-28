package com.example.clearsolutiontesttask.exception;

import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException {

  private final String ErrorCode;

  public ResourceConflictException(String message, String errorCode) {
    super(message);
    ErrorCode = errorCode;
  }
}
