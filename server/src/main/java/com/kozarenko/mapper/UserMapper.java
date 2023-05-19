package com.kozarenko.mapper;

import com.kozarenko.dto.user.UserCredentialsDto;
import com.kozarenko.dto.user.UserResponseDto;
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

  public UserResponseDto mapToUserResponseDto(User user, String jwt) {
    UserResponseDto userDto = modelMapper.map(user, UserResponseDto.class);
    userDto.setJwt(jwt);
    return userDto;
  }
}
