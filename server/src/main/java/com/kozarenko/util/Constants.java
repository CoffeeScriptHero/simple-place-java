package com.kozarenko.util;


public class Constants {

  public static class Path {
    public static final String H2_PATH = "/h2-console";
    public static final String AUTHENTICATION_PATH = "/api/authentication";
  }

  public static class Cloudinary {
    public static final String CLOUDINARY_SCHEME = "cloudinary://";
    public static final String CLOUD_NAME = "CLOUD_NAME";
    public static final String API_KEY = "API_KEY";
    public static final String API_SECRET = "API_SECRET";
  }

  public static class Auth {
    public static final String NO_SUCH_USERNAME = "User with such username does not exist";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
  }

  public static class Exception {
    public static final String NO_REQUEST_PARAMETER = "Missing request parameter";
    public static final String USER_NOT_FOUND = "User with such username was not found";
    public static final String USERNAME_TAKEN = "User with such username already exists.";
    public static final String WRONG_PASSWORD = "An incorrect password was entered";
  }
}
