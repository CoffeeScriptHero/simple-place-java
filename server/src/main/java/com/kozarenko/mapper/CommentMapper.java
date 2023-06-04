package com.kozarenko.mapper;

import com.kozarenko.dto.comment.CommentInfoDto;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.User;
import com.kozarenko.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

  private final ModelMapper modelMapper;
  private final UserMapper userMapper;
  private final CommentLikeService commentLikeService;

  public List<CommentInfoDto> mapForListing(List<Comment> comments, User currentUser) {
    return comments.stream()
            .map(c -> mapForCommentInfoDto(c, currentUser))
            .toList();
  }

  public CommentInfoDto mapForCommentInfoDto(Comment comment, User currentUser) {
    CommentInfoDto commentDto = modelMapper.map(comment, CommentInfoDto.class);
    commentDto.setAuthor(userMapper.mapToUserInfoDto(comment.getUser()));
    commentDto.setLikesNumber(commentLikeService.countCommentLikes(comment.getId()));
    commentDto.setLiked(commentLikeService.existsByIds(currentUser.getId(), comment.getId()));
    return commentDto;
  }
}
