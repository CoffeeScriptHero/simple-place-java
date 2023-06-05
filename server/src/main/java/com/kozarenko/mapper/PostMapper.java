package com.kozarenko.mapper;

import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.CommentService;
import com.kozarenko.service.FollowingService;
import com.kozarenko.service.PostLikeService;
import com.kozarenko.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

  private final ModelMapper modelMapper;
  private final CommentMapper commentMapper;
  private final UserService userService;
  private final CommentService commentService;
  private final FollowingService followingService;
  private final PostLikeService postLikeService;

  public PostMapper(
          ModelMapper modelMapper,
          CommentMapper commentMapper,
          UserService userService,
          CommentService commentService,
          FollowingService followingService,
          PostLikeService postLikeService) {
    this.modelMapper = modelMapper;
    this.commentMapper = commentMapper;
    this.userService = userService;
    this.commentService = commentService;
    this.followingService = followingService;
    this.postLikeService = postLikeService;
    TypeMap<PostCreationDto, Post> propertyMapper = modelMapper.createTypeMap(PostCreationDto.class, Post.class);
    propertyMapper.addMappings(mapper -> mapper.skip(Post::setImageUrl));
    TypeMap<Post, PostInfoDto> propertyMapper2 = modelMapper.createTypeMap(Post.class, PostInfoDto.class);
    propertyMapper2.addMappings(mapper -> mapper.skip(PostInfoDto::setLikes));
    propertyMapper2.addMappings(mapper -> mapper.skip(PostInfoDto::setComments));
  }

  public Post mapToPost(PostCreationDto postDto) {
    return modelMapper.map(postDto, Post.class);
  }

  public List<PostInfoDto> mapForListing(List<Post> posts, String username) {
    return posts.stream()
            .map(p -> {
              try {
                return mapToPostInfoDto(p, username);
              } catch (NoUserWithSuchUsernameException | NoPostWithSuchIdException e) {
                throw new RuntimeException(e);
              }
            })
            .toList();
  }

  public PostInfoDto mapToPostInfoDto(Post post, String username)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException {
    User currentUser = userService.findByUsername(username);
    PostInfoDto postDto = modelMapper.map(post, PostInfoDto.class);
    setPostInfoDetails(postDto, currentUser);
    return postDto;
  }

  private void setPostInfoDetails(PostInfoDto postDto, User currentUser)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException {
    String id = postDto.getId();
    User author = userService.findByUsername(postDto.getAuthor().getUsername());
    postDto.getAuthor().setFollowed(followingService.isUserFollowing(currentUser, author));
    postDto.setLiked(postLikeService.existsByIds(currentUser.getId(), id));
    postDto.setLikes(postLikeService.getUsersListOfLikes(id).stream().map(User::getId).toList());
    postDto.setComments(commentMapper.mapForListing(commentService.getComments(id), currentUser));
  }
}
