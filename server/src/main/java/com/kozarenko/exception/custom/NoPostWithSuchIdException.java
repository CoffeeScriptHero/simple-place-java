package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.POST_NOT_FOUND;

public class NoPostWithSuchIdException extends Exception{
  public NoPostWithSuchIdException() {
    super(POST_NOT_FOUND);
  }
}
