package com.kozarenko.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserAccountDto {
  private String id;
  private String username;
  private String profileImg;
  private String jwt;
  private List<String> following;
  private List<String> followers;
}
