package com.kozarenko.dto.user;

import com.kozarenko.dto.post.PostInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDto {
  private String username;
  private String profileImg;
  private String id;
  private List<String> following;
  private List<String> followers;
  private List<PostInfoDto> posts;
}
