package com.kozarenko.controller;

import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.dto.user.*;
import com.kozarenko.exception.custom.AccessDeniedException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.exception.custom.UsernameTakenException;
import com.kozarenko.mapper.PostMapper;
import com.kozarenko.mapper.UserMapper;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.CloudinaryService;
import com.kozarenko.service.FollowingService;
import com.kozarenko.service.PostService;
import com.kozarenko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kozarenko.util.Constants.Cloudinary.DEFAULT_IMG;
import static com.kozarenko.util.Constants.Cloudinary.DEFAULT_PUBLIC_ID;
import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;
import static com.kozarenko.util.Constants.Request.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final CloudinaryService cloudinaryService;
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

  @PatchMapping("/profile/username")
  public ResponseEntity<Void> changeUsername(
          @RequestBody UsernameDto usernameDto,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws UsernameTakenException, NoUserWithSuchUsernameException {
    if (userService.existsByUsername(usernameDto.getUsername())) {
      throw new UsernameTakenException();
    }

    findByUsername(username).setUsername(usernameDto.getUsername());

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/profile/avatar")
  public ResponseEntity<UserImageDto> changeProfileImg(
          @RequestBody UserImageDto userDto,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, IOException {
    User user = findByUsername(username);
    deleteProfilePic(user);
    String imageUrl = cloudinaryService.uploadProfilePic(userDto.getImage(), user.getId());
    user.setProfileImgUrl(imageUrl);

    return ResponseEntity.ok(new UserImageDto(imageUrl));
  }

  @DeleteMapping("/profile/avatar")
  public ResponseEntity<UserImageDto> deleteProfileImg(
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, IOException {
    User user = findByUsername(username);
    if (deleteProfilePic(user)) {
      user.setProfileImgUrl(DEFAULT_IMG);
    }

    return ResponseEntity.ok(new UserImageDto(user.getProfileImgUrl()));
  }

  @PostMapping("/{username}")
  public ResponseEntity<Void> followUser(
          @PathVariable(USERNAME_QUERY) String username,
          @RequestAttribute(USERNAME_ATTRIBUTE) String currentUserUsername) throws NoUserWithSuchUsernameException {
    return new ResponseEntity<>(
            followingService.followUnfollow(findByUsername(username), findByUsername(currentUserUsername))
            ? HttpStatus.CREATED
            : HttpStatus.OK
    );
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(
          @PathVariable(USERNAME_QUERY) String username,
          @RequestAttribute(USERNAME_ATTRIBUTE) String currentUserUsername) throws NoUserWithSuchUsernameException {
    followingService.deleteFollower(findByUsername(username), findByUsername(currentUserUsername));
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private boolean deleteProfilePic(User user) throws IOException {
    if (!user.getProfileImgUrl().contains(DEFAULT_PUBLIC_ID)) {
      cloudinaryService.deleteProfilePic(extractPublicId(user.getProfileImgUrl()));
      return true;
    }

    return false;
  }

  private User findByUsername(String username) throws NoUserWithSuchUsernameException {
    return userService.findByUsername(username);
  }

  private String extractPublicId(String imageUrl) throws MalformedURLException {
    URL url = new URL(imageUrl);
    String path = url.getPath();
    int index = path.lastIndexOf('/');
    String image = path.substring(index + 1);
    return image.substring(0, image.lastIndexOf('.'));
  }
}
