package com.example.clearsolutiontesttask.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
  private final String ErrorCode;

  public NotFoundException(String message, String errorCode) {
    super(message);
    ErrorCode = errorCode;
  }
}
