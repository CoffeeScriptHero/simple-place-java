package com.kozarenko.util;


public class Constants {

  public static class Path {
    public static final String H2_PATH = "/h2-console";
    public static final String LOGIN_PATH = "/api/authentication/login";
    public static final String REGISTRATION_PATH = "/api/authentication/registration";
  }

  public static class Cloudinary {
    public static final String DEFAULT_IMG = "https://res.cloudinary.com/drrhht2jy/image/upload/v1654873620/simple-place-friends/profilePics/default.jpg";
    public static final String DEFAULT_PUBLIC_ID = "profilePics/default";
    public static final String CLOUDINARY_SCHEME = "cloudinary://";
    public static final String CLOUD_NAME = "CLOUD_NAME";
    public static final String API_KEY = "API_KEY";
    public static final String API_SECRET = "API_SECRET";
    public static final String PROFILE_PICS_PRESET = "profilePicsPreset";
    public static final String POSTS_PRESET = "postsPreset";
    public static final String POSTS_FOLDER = "posts/";
    public static final String PROFILE_PICS_FOLDER = "profilePics/";
  }

  public static class Request {
    public static final String OPTIONS_METHOD = "OPTIONS";
    public static final String SEARCH_QUERY = "s";
    public static final String USERNAME_QUERY = "username";
    public static final String ID_QUERY = "id";
    public static final String PAGE_NUMBER_QUERY = "p";
    public static final String RESULTS_PER_PAGE_QUERY = "n";
    public static final Integer PAGE_NUMBER_DEFAULT = 0;
    public static final Integer POSTS_PER_PAGE_DEFAULT = 3;
  }

  public static class Auth {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USERNAME_ATTRIBUTE = "username";
    public static final String BEARER = "Bearer ";
  }

  public static class Exception {
    public static final String NO_REQUEST_PARAMETER = "Missing request parameter";
    public static final String USER_NOT_FOUND = "User with such username was not found";
    public static final String USERNAME_TAKEN = "User with such username already exists";
    public static final String WRONG_PASSWORD = "An incorrect password was entered";
    public static final String COMMENT_NOT_FOUND = "Comment with such id was not found";
    public static final String POST_NOT_FOUND = "Post with such id was not found";
    public static final String ACCESS_DENIED = "Access denied.";
  }
}
