package com.kozarenko.dto.user;

import lombok.Data;

@Data
public class UserCredentialsDto {
  private String username;
  private String password;
  private boolean rememberMe;
}
