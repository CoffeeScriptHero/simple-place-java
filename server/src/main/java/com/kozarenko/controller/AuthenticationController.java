package com.kozarenko.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kozarenko.dto.user.UserCredentialsDto;
import com.kozarenko.dto.user.UserAccountDto;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.exception.custom.PasswordVerificationException;
import com.kozarenko.exception.custom.UsernameTakenException;
import com.kozarenko.mapper.UserMapper;
import com.kozarenko.model.base.User;
import com.kozarenko.service.UserService;
import com.kozarenko.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kozarenko.util.Constants.Auth.USERNAME_ATTRIBUTE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {

  private final UserService userService;

  private final UserMapper userMapper;

  private final JwtTokenUtil jwtTokenUtil;

  @PostMapping("registration")
  public ResponseEntity<UserAccountDto> saveUser(@RequestBody UserCredentialsDto userDto) throws
          UsernameTakenException {
    userService.checkUsernameTaken(userDto.getUsername());
    User user = userMapper.mapToUser(userDto);
    user.setPassword(encode(userDto.getPassword()));
    userService.save(user);

    return new ResponseEntity<>(getUserResponseDto(user, userDto), HttpStatus.CREATED);
  }

  @PostMapping("login")
  public ResponseEntity<UserAccountDto> logIn(@RequestBody UserCredentialsDto userDto)
          throws NoUserWithSuchUsernameException,
          PasswordVerificationException {
    User user = userService.findByUsername(userDto.getUsername()).orElseThrow(NoUserWithSuchUsernameException::new);
    verify(user.getPassword(), userDto.getPassword());

    return new ResponseEntity<>(getUserResponseDto(user, userDto), HttpStatus.OK);
  }

  @GetMapping("jwt")
  public ResponseEntity<UserAccountDto> logInByJwt(@RequestAttribute(USERNAME_ATTRIBUTE) String username)
          throws NoUserWithSuchUsernameException {
    System.out.println(username);
    User user = userService.findByUsername(username).orElseThrow(NoUserWithSuchUsernameException::new);

    return new ResponseEntity<>(userMapper.mapToUserAccountDto(user), HttpStatus.OK);
  }

  private UserAccountDto getUserResponseDto(User user, UserCredentialsDto userDto) {
    return userMapper.mapToUserAccountDto(
            user, createJwt(userDto.getUsername(), userDto.isRememberMe()));
  }

  private void verify(String hashPassword, String password) throws PasswordVerificationException {
    if (!BCrypt.verifyer().verify(password.toCharArray(), hashPassword).verified) {
      throw new PasswordVerificationException();
    }
  }

  private String encode(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  private String createJwt(String username, boolean isRememberMe) {
    return jwtTokenUtil.generateToken(username, isRememberMe);
  }
}
