package com.kozarenko.dto.comment;

import com.kozarenko.dto.user.UserInfoDto;
import lombok.Data;

@Data
public class CommentInfoDto {
  private String id;
  private UserInfoDto author;
  private String text;
  private int likesNumber;
  private boolean isLiked;
}
