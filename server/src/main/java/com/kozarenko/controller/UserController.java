package com.kozarenko.controller;

import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.dto.user.*;
import com.kozarenko.exception.custom.AccessDeniedException;
import com.kozarenko.exception.custom.NoUserWithSuchIdException;
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
import com.kozarenko.util.JwtTokenUtil;
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
  private final JwtTokenUtil jwtTokenUtil;

  @GetMapping("/{username}/profile")
  public ResponseEntity<UserProfileDto> getUserProfile(
          @PathVariable(USERNAME_QUERY) String usernameParam,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    User user = userService.findByUsername(usernameParam);
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

  @GetMapping("/{username}/followers")
  public ResponseEntity<List<UserAuthorDto>> getFollowers(
          @PathVariable(USERNAME_QUERY) String usernameParam,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    return ResponseEntity.ok(userMapper.mapForListing(
            followingService.getFollowers(findByUsername(usernameParam)), username)
    );
  }

  @GetMapping("/{username}/following")
  public ResponseEntity<List<UserAuthorDto>> getFollowing(
          @PathVariable(USERNAME_QUERY) String usernameParam,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username) throws NoUserWithSuchUsernameException {
    return ResponseEntity.ok(userMapper.mapForListing(
            followingService.getFollowings(findByUsername(usernameParam)), username)
    );
  }

  @GetMapping("/search")
  public ResponseEntity<List<UserInfoDto>> getSearchedUsers(@RequestParam(SEARCH_QUERY) String input) {
    return ResponseEntity.ok(
            Objects.equals(input, "") || Objects.equals(input, ".")
                    ? new ArrayList<>()
                    : userMapper.mapForListing(userService.findByRegex(input)));
  }

  @PatchMapping("/profile/username")
  public ResponseEntity<String> changeUsername(
          @RequestBody UsernameDto usernameDto,
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws UsernameTakenException, NoUserWithSuchUsernameException {
    if (userService.existsByUsername(usernameDto.getUsername())) {
      throw new UsernameTakenException();
    }
    User user = findByUsername(username);
    user.setUsername(usernameDto.getUsername());
    userService.save(user);

    return new ResponseEntity<>(jwtTokenUtil.generateToken(usernameDto.getUsername(), false), HttpStatus.OK);
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
    userService.save(user);
    UserImageDto userDtoResponse = new UserImageDto();
    userDtoResponse.setImage(imageUrl);

    return ResponseEntity.ok(userDtoResponse);
  }

  @DeleteMapping("/profile/avatar")
  public ResponseEntity<UserImageDto> deleteProfileImg(
          @RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException, IOException {
    User user = findByUsername(username);
    if (deleteProfilePic(user)) {
      user.setProfileImgUrl(DEFAULT_IMG);
      userService.save(user);
    }
    UserImageDto userDtoResponse = new UserImageDto();
    userDtoResponse.setImage(user.getProfileImgUrl());

    return ResponseEntity.ok(userDtoResponse);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Void> followUser(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String currentUserUsername)
          throws NoUserWithSuchUsernameException, NoUserWithSuchIdException {
    return new ResponseEntity<>(
            followingService.followUnfollow(userService.findById(id), findByUsername(currentUserUsername))
                    ? HttpStatus.CREATED
                    : HttpStatus.OK
    );
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
          @PathVariable(ID_QUERY) String id,
          @RequestAttribute(USERNAME_ATTRIBUTE) String currentUserUsername)
          throws NoUserWithSuchUsernameException, NoUserWithSuchIdException {
    followingService.deleteFollower(userService.findById(id), findByUsername(currentUserUsername));
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
