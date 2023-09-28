package com.example.clearsolutiontesttask.exception;

import lombok.Getter;

@Getter
public class ParametersException extends RuntimeException{

  private final String errorCode;

    public ParametersException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
