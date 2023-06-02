package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.WRONG_PASSWORD;

public class PasswordVerificationException extends Exception {
  public PasswordVerificationException() {
    super(WRONG_PASSWORD);
  }
}
