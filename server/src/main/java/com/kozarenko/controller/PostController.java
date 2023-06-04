package com.kozarenko.controller;

import com.kozarenko.dto.comment.CommentInfoDto;
import com.kozarenko.dto.comment.CommentTextDto;
import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.dto.user.UserAuthorDto;
import com.kozarenko.exception.custom.AccessDeniedException;
import com.kozarenko.exception.custom.NoCommentWithSuchIdException;
import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.mapper.CommentMapper;
import com.kozarenko.mapper.PostMapper;
import com.kozarenko.mapper.UserMapper;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;
import static com.kozarenko.util.Constants.Request.ID_QUERY;
import static com.kozarenko.util.Constants.Request.PAGE_NUMBER_QUERY;
import static com.kozarenko.util.Constants.Request.RESULTS_PER_PAGE_QUERY;
import static com.kozarenko.util.Constants.Request.PAGE_NUMBER_DEFAULT;
import static com.kozarenko.util.Constants.Request.POSTS_PER_PAGE_DEFAULT;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final CloudinaryService cloudinaryService;
  private final UserService userService;
  private final PostService postService;
  private final PostLikeService postLikeService;
  private final CommentLikeService commentLikeService;
  private final CommentService commentService;
  private final PostMapper postMapper;
  private final UserMapper userMapper;
  private final CommentMapper commentMapper;

  @PostMapping()
  public ResponseEntity<PostInfoDto> createPost(@RequestAttribute(USERNAME_ATTRIBUTE) String username,
                                                @RequestBody PostCreationDto postDto)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException {
    User user = userService.findByUsername(username);
    Post post = postService.save(postMapper.mapToPost(postDto));
    String imageUrl = cloudinaryService.uploadPostPic(postDto.getBase64Image(), post.getId());

    post.setAuthor(user);
    post.setImageUrl(imageUrl);

    return new ResponseEntity<>(postMapper.mapToPostInfoDto(postService.save(post), username), HttpStatus.CREATED);
  }

  @GetMapping("")
  public ResponseEntity<List<PostInfoDto>>
  getPosts(@RequestParam(PAGE_NUMBER_QUERY) Optional<Integer> pageParam,
           @RequestParam(RESULTS_PER_PAGE_QUERY) Optional<Integer> postsPerPageParam,
           @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException {
    userService.existsByUsername(username);
    int page = pageParam.orElse(PAGE_NUMBER_DEFAULT);
    int postsPerPage = postsPerPageParam.orElse(POSTS_PER_PAGE_DEFAULT);
    return new ResponseEntity<>(postMapper.mapForListing(
            postService.getPosts(page, postsPerPage),
            username
    ), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public PostInfoDto getPostById(@PathVariable(ID_QUERY) String id,
                                 @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoPostWithSuchIdException, NoUserWithSuchUsernameException {
    return postMapper.mapToPostInfoDto(postService.getReferenceById(id), username);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable(ID_QUERY) String id,
                                      @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoPostWithSuchIdException, IOException, AccessDeniedException {
    Post post = postService.getReferenceById(id);

    authenticatePost(post, username);
    cloudinaryService.deletePostPic(post.getId());
    postService.delete(post);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}/likes")
  public ResponseEntity<List<UserAuthorDto>> getPostLikes(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException {

    return new ResponseEntity<>(
            userMapper.mapForListing(postLikeService.getUsersListOfLikes(id), username),
            HttpStatus.OK
    );
  }

  @GetMapping("/{id}/comments/likes")
  public ResponseEntity<List<UserAuthorDto>> getCommentLikes(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, NoCommentWithSuchIdException {

    return new ResponseEntity<>(
            userMapper.mapForListing(commentLikeService.getUsersListOfLikes(id), username),
            HttpStatus.OK
    );
  }

  @PostMapping("/{id}/likes")
  public ResponseEntity<Integer> savePostLike(@PathVariable(ID_QUERY) String id,
                                              @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException {
    return new ResponseEntity<>(
            postLikeService.countPostLikes(id),
            postLikeService.saveLike(userService.findByUsername(username).getId(), id)
                    ? HttpStatus.CREATED
                    : HttpStatus.OK
    );
  }

  @PostMapping("/{id}/comments/likes")
  public ResponseEntity<Integer> saveCommentLike(@PathVariable(ID_QUERY) String id,
                                                 @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException {
    return new ResponseEntity<>(
            commentLikeService.countCommentLikes(id),
            commentLikeService.saveLike(userService.findByUsername(username).getId(), id)
                    ? HttpStatus.CREATED
                    : HttpStatus.OK
    );
  }

  @PostMapping("/{id}/comments")
  public ResponseEntity<List<CommentInfoDto>> createComment(@PathVariable(ID_QUERY) String id,
                                                            @RequestBody CommentTextDto commentDto,
                                                            @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException {
    User user = userService.findByUsername(username);
    List<Comment> comments = commentService.save(new Comment(
            user,
            postService.getReferenceById(id),
            commentDto.getText()
    ));
    return new ResponseEntity<>(commentMapper.mapForListing(comments, user), HttpStatus.CREATED);
  }

  @DeleteMapping("/comments/{id}")
  public ResponseEntity<List<CommentInfoDto>> deleteComment(@PathVariable(ID_QUERY) String id,
                                                            @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, NoPostWithSuchIdException, AccessDeniedException {
    return new ResponseEntity<>(
            commentMapper.mapForListing(commentService.delete(id, username), userService.findByUsername(username)),
            HttpStatus.CREATED
    );
  }

  private void authenticatePost(Post post, String username) throws AccessDeniedException {
    if (!username.equals(post.getAuthor().getUsername())) {
      throw new AccessDeniedException();
    }
  }
}
