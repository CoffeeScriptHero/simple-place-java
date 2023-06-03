package com.kozarenko.dto.post;

import com.kozarenko.dto.user.UserAuthorDto;
import com.kozarenko.model.additional.PostLike;
import com.kozarenko.model.base.Comment;
import lombok.Data;

import java.util.List;

@Data
public class PostInfoDto {
  private String id;
  private String description;
  private String imageUrl;
  private UserAuthorDto author;
  private List<PostLike> likes;
  private List<Comment> comments;
}
