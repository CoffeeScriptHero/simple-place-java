package com.kozarenko.controller;

import com.kozarenko.dto.Response;
import com.kozarenko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationController {

  private static final String USERNAME_ALREADY_EXISTS = "User with such username already exists.";

  private final UserService userService;

  @GetMapping("check-username")
  public ResponseEntity<?> checkIfUsernameExists(@RequestParam("u") String username) {
    return userService.existsByUsername(username)
            ? ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(USERNAME_ALREADY_EXISTS))
            : ResponseEntity.ok().build();
  }
}
