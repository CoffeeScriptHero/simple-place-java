package com.kozarenko.mapper;

import com.kozarenko.dto.comment.CommentInfoDto;
import com.kozarenko.dto.user.UserProfileDto;
import com.kozarenko.exception.custom.NoCommentWithSuchIdException;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.User;
import com.kozarenko.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {

  private final ModelMapper modelMapper;
  private final UserMapper userMapper;
  private final CommentLikeService commentLikeService;

  public CommentMapper(ModelMapper modelMapper, UserMapper userMapper, CommentLikeService commentLikeService) {
    this.modelMapper = modelMapper;
    this.userMapper = userMapper;
    this.commentLikeService = commentLikeService;
    TypeMap<Comment, CommentInfoDto> propertyMapper = modelMapper.createTypeMap(Comment.class, CommentInfoDto.class);
    propertyMapper.addMappings(mapper -> mapper.skip(CommentInfoDto::setLikes));
  }

  public List<CommentInfoDto> mapForListing(List<Comment> comments, User currentUser) {
    return comments.stream()
            .map(c -> {
              try {
                return mapForCommentInfoDto(c, currentUser);
              } catch (NoCommentWithSuchIdException e) {
                throw new RuntimeException(e);
              }
            })
            .toList();
  }

  public CommentInfoDto mapForCommentInfoDto(Comment comment, User currentUser) throws NoCommentWithSuchIdException {
    CommentInfoDto commentDto = modelMapper.map(comment, CommentInfoDto.class);
    commentDto.setAuthor(userMapper.mapToUserInfoDto(comment.getUser()));
    commentDto.setLikes(commentLikeService.getUsersListOfLikes(comment.getId()).stream().map(User::getId).toList());
    commentDto.setLiked(commentLikeService.existsByIds(currentUser.getId(), comment.getId()));
    return commentDto;
  }
}
