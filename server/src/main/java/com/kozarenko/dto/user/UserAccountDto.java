package com.kozarenko.dto.user;

import lombok.Data;

@Data
public class UserAccountDto {
  private String id;
  private String profileImg;
  private String jwt;
}
