package com.kozarenko.controller;

import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.dto.user.UserAuthorDto;
import com.kozarenko.dto.user.UserInfoDto;
import com.kozarenko.dto.user.UserProfileDto;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.mapper.PostMapper;
import com.kozarenko.mapper.UserMapper;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.FollowingService;
import com.kozarenko.service.PostService;
import com.kozarenko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;
import static com.kozarenko.util.Constants.Request.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final PostService postService;
  private final FollowingService followingService;
  private final UserMapper userMapper;
  private final PostMapper postMapper;

  @GetMapping("/{username}/profile")
  public ResponseEntity<UserProfileDto> getUserProfile(
          @PathVariable(USERNAME_QUERY) String usernameParam,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    User user = userService.findByUsername(username);
    List<PostInfoDto> postDtos = postMapper.mapForListing(postService.getPostsByUsername(usernameParam), username);

    return ResponseEntity.ok(userMapper.mapForUserProfileDto(user, postDtos, username));
  }

  @GetMapping("/recommended")
  public ResponseEntity<List<UserAuthorDto>> getRecommendedUsers(@RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException {
    List<User> suggestedUsers = followingService.getSuggestedUsers(userService.findByUsername(username))
            .stream()
            .map(u -> userService.getReferenceById(u.getId()))
            .toList();

    return ResponseEntity.ok(userMapper.mapForListing(suggestedUsers, username));
  }

  @GetMapping("/{id}/followers")
  public ResponseEntity<List<UserAuthorDto>> getFollowers(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    return ResponseEntity.ok(userMapper.mapForListing(followingService.getFollowers(id), username));
  }

  @GetMapping("/{id}/following")
  public ResponseEntity<List<UserAuthorDto>> getFollowing(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    return ResponseEntity.ok(userMapper.mapForListing(followingService.getFollowings(id), username));
  }

  @GetMapping("/search")
  public ResponseEntity<List<UserInfoDto>> getSearchedUsers(@RequestParam(SEARCH_QUERY) String input) {
    return ResponseEntity.ok(
            Objects.equals(input, "") || Objects.equals(input, ".")
                    ? new ArrayList<>()
                    : userMapper.mapForListing(userService.findByRegex(input)));
  }
}
