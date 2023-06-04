package com.kozarenko.dto.user;

import lombok.Data;

@Data
public class UserAuthorDto {
  private String id;
  private String username;
  private String profileImgUrl;
  private boolean isFollowed;
}
