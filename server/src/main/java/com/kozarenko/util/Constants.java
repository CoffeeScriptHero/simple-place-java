package com.kozarenko.util;

public class Constants {

  public static class Auth {
    public static final String USERNAME_TAKEN = "User with such username already exists.";
    public static final String NO_SUCH_USERNAME = "User with such username does not exist";
    public static final String WRONG_PASSWORD = "You entered an incorrect password.";
    public static final String BEARER = "Bearer ";
  }

  public static class Exception {
    public static final String NO_REQUEST_PARAMETER = "Missing request parameter";
    public static final String USER_NOT_FOUND = "User with such username was not found.";
  }
}
