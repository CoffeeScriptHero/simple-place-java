package com.kozarenko.mapper;

import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.dto.user.*;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.service.FollowingService;
import com.kozarenko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

  private final ModelMapper modelMapper;
  private final UserService userService;
  private final FollowingService followingService;

  public UserMapper(ModelMapper modelMapper, UserService userService, FollowingService followingService) {
    this.modelMapper = modelMapper;
    this.userService = userService;
    this.followingService = followingService;
    TypeMap<User, UserProfileDto> propertyProfileMapper = modelMapper.createTypeMap(User.class, UserProfileDto.class);
    propertyProfileMapper.addMappings(mapper -> mapper.skip(UserProfileDto::setFollowers));
    propertyProfileMapper.addMappings(mapper -> mapper.skip(UserProfileDto::setFollowing));
    propertyProfileMapper.addMappings(mapper -> mapper.skip(UserProfileDto::setPosts));
    TypeMap<User, UserAccountDto> propertyAccountMapper = modelMapper.createTypeMap(User.class, UserAccountDto.class);
    propertyAccountMapper.addMappings(mapper -> mapper.skip(UserAccountDto::setFollowing));
    propertyAccountMapper.addMappings(mapper -> mapper.skip(UserAccountDto::setFollowers));
  }

  public User mapToUser(UserCredentialsDto userDto) {
    return modelMapper.map(userDto, User.class);
  }

  public UserProfileDto mapForUserProfileDto(User user, List<PostInfoDto> postDtos, String username)
          throws NoUserWithSuchUsernameException {
    UserProfileDto userDto = modelMapper.map(user, UserProfileDto.class);
    userDto.setPosts(postDtos);
    setAccountDetails(userDto, userService.findByUsername(username));
    return userDto;
  }

  public List<UserInfoDto> mapForListing(List<User> users)  {
    return users.stream()
            .map(this::mapToUserInfoDto)
            .toList();
  }

  public List<UserAuthorDto> mapForListing(List<User> users, String username) throws NoUserWithSuchUsernameException {
    User currentUser = userService.findByUsername(username);

    return users.stream()
            .map(u -> mapToUserAuthorDto(u, currentUser))
            .toList();
  }

  public UserAuthorDto mapToUserAuthorDto(User user, User currentUser) {
    UserAuthorDto userDto = modelMapper.map(user, UserAuthorDto.class);
    userDto.setFollowed(followingService.isUserFollowing(currentUser, user));

    return userDto;
  }

  public UserInfoDto mapToUserInfoDto(User user) {
    return modelMapper.map(user, UserInfoDto.class);
  }

  public UserAccountDto mapToUserAccountDto(User user, String jwt) {
    UserAccountDto userDto = modelMapper.map(user, UserAccountDto.class);
    userDto.setJwt(jwt);
    setAccountDetails(userDto, user);

    return userDto;
  }

  public UserAccountDto mapToUserAccountDto(User user) {
    UserAccountDto userDto = modelMapper.map(user, UserAccountDto.class);
    setAccountDetails(userDto, user);

    return userDto;
  }

  private void setAccountDetails(UserProfileDto userDto, User user) {
    userDto.setFollowers(extractUserIds(followingService.getFollowers(userDto.getId())));
    userDto.setFollowing(extractUserIds(followingService.getFollowings(userDto.getId())));
  }

  private void setAccountDetails(UserAccountDto userDto, User user) {
    userDto.setFollowers(extractUserIds(followingService.getFollowers(user.getId())));
    userDto.setFollowing(extractUserIds(followingService.getFollowings(user.getId())));
  }

  private List<String> extractUserIds(List<User> users) {
    return users.stream()
            .map(User::getId)
            .toList();
  }
}
