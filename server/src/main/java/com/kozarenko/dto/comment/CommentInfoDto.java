package com.kozarenko.dto.comment;

import com.kozarenko.dto.user.UserInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentInfoDto {
  private String id;
  private UserInfoDto author;
  private String text;
  private boolean isLiked;
  private List<String> likes;
}
