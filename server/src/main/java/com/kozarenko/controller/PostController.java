package com.kozarenko.controller;

import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.exception.custom.AccessDeniedException;
import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.mapper.PostMapper;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.CloudinaryService;
import com.kozarenko.service.PostService;
import com.kozarenko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;
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
  private final PostMapper postMapper;

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

  @DeleteMapping("{id}")
  public ResponseEntity<?> deletePost(@PathVariable("id") String id,
                                      @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoPostWithSuchIdException, IOException, AccessDeniedException {

    Post post = postService.getReferenceById(id);

    authenticatePost(post, username);
    cloudinaryService.deletePostPic(post.getId());
    postService.delete(post);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private void authenticatePost(Post post, String username) throws AccessDeniedException {
    if (!username.equals(post.getAuthor().getUsername())) {
      throw new AccessDeniedException();
    }
  }
}
