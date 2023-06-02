package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.USERNAME_TAKEN;

public class UsernameTakenException extends Exception{
  public UsernameTakenException() {
    super(USERNAME_TAKEN);
  }
}
