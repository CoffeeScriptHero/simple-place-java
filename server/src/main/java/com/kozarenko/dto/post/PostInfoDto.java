package com.kozarenko.dto.post;

import com.kozarenko.dto.comment.CommentInfoDto;
import com.kozarenko.dto.user.UserAuthorDto;
import lombok.Data;

import java.util.List;

@Data
public class PostInfoDto {
  private String id;
  private String description;
  private String imageUrl;
  private UserAuthorDto author;
  private boolean isLiked;
  private int likesNumber;
  private List<CommentInfoDto> comments;
}
