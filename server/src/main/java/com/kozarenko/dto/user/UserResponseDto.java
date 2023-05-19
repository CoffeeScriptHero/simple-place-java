package com.kozarenko.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {

  private int id;

  private String profileImg;

  private String jwt;
}
