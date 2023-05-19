package com.kozarenko.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kozarenko.dto.user.UserCredentialsDto;
import com.kozarenko.dto.user.UserResponseDto;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.mapper.UserMapper;
import com.kozarenko.model.base.User;
import com.kozarenko.service.UserService;
import com.kozarenko.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kozarenko.util.Constants.Auth.USERNAME_TAKEN;
import static com.kozarenko.util.Constants.Auth.WRONG_PASSWORD;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {

  private final UserService userService;

  private final UserMapper userMapper;

  private final JwtTokenUtil jwtTokenUtil;

  @PostMapping("registration")
  public ResponseEntity<?> saveUser(@RequestBody UserCredentialsDto userDto) {
    if (userService.existsByUsername(userDto.getUsername())) {
      return new ResponseEntity<>(USERNAME_TAKEN, HttpStatus.CONFLICT);
    }

    User user = userMapper.mapToUser(userDto);
    user.setPassword(encode(userDto.getPassword()));
    userService.save(user);

    return userResponseDto(user, userDto, HttpStatus.CREATED);
  }

  @PostMapping("login")
  public ResponseEntity<?> logIn(@RequestBody UserCredentialsDto userDto) throws NoUserWithSuchUsernameException {
    User user = userService.findByUsername(userDto.getUsername()).orElseThrow(NoUserWithSuchUsernameException::new);

    if (!verify(user.getPassword(), userDto.getPassword())) {
      return new ResponseEntity<>(WRONG_PASSWORD, HttpStatus.UNAUTHORIZED);
    }

    return userResponseDto(user, userDto, HttpStatus.OK);
  }

  private ResponseEntity<UserResponseDto> userResponseDto(User user, UserCredentialsDto userDto, HttpStatus status) {
    return new ResponseEntity<>(userMapper.mapToUserResponseDto(
            user, createJwt(userDto.getUsername(), userDto.isRememberMe())), status);
  }

  private boolean verify(String hashPassword, String password) {
    return BCrypt.verifyer().verify(password.toCharArray(), hashPassword).verified;
  }

  private String encode(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  private String createJwt(String username, boolean isRememberMe) {
    return jwtTokenUtil.generateToken(username, isRememberMe);
  }
}
