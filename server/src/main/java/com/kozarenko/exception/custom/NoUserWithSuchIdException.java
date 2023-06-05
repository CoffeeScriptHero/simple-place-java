package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.USER_NOT_FOUND;

public class NoUserWithSuchIdException extends Exception {
  public NoUserWithSuchIdException() {
    super(USER_NOT_FOUND);
  }
}
