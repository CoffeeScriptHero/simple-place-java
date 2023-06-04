package com.kozarenko.mapper;

import com.kozarenko.dto.user.UserCredentialsDto;
import com.kozarenko.dto.user.UserAccountDto;
import com.kozarenko.dto.user.UserInfoDto;
import com.kozarenko.model.base.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final ModelMapper modelMapper;

  public User mapToUser(UserCredentialsDto userDto) {
    return modelMapper.map(userDto, User.class);
  }

  public UserInfoDto mapToUserInfoDto(User user) {
    return modelMapper.map(user, UserInfoDto.class);
  }

  public UserAccountDto mapToUserAccountDto(User user, String jwt) {
    UserAccountDto userDto = modelMapper.map(user, UserAccountDto.class);
    userDto.setJwt(jwt);

    return userDto;
  }

  public UserAccountDto mapToUserAccountDto(User user) {
    return modelMapper.map(user, UserAccountDto.class);
  }
}
