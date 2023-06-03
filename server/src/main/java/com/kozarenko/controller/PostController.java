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

import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;

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
          throws NoUserWithSuchUsernameException {
    User user = userService.findByUsername(username).orElseThrow(NoUserWithSuchUsernameException::new);
    Post post = postService.save(postMapper.mapToPost(postDto));
    String imageUrl = cloudinaryService.uploadPostPic(postDto.getBase64Image(), post.getId());

    post.setAuthor(user);
    post.setImageUrl(imageUrl);

    return new ResponseEntity<>(postMapper.mapToPostInfoDto(postService.save(post)), HttpStatus.CREATED);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deletePost(@PathVariable("id") String id,
                         @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoPostWithSuchIdException, IOException, AccessDeniedException {

    Post post = postService.getReferenceById(id);

    authenticatePost(post, username);
    cloudinaryService.deletePic(post.getId());
    postService.delete(post);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private void authenticatePost(Post post, String username) throws AccessDeniedException {
    if (!username.equals(post.getAuthor().getUsername())) {
      throw new AccessDeniedException();
    }
  }
}
