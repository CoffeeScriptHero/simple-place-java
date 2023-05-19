package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.USER_NOT_FOUND;

public class NoUserWithSuchUsernameException extends Exception {
  public NoUserWithSuchUsernameException() {
    super(USER_NOT_FOUND);
  }
}
