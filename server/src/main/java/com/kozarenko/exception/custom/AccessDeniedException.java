package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.ACCESS_DENIED;

public class AccessDeniedException extends Exception{
  public AccessDeniedException() {
    super(ACCESS_DENIED);
  }
}
