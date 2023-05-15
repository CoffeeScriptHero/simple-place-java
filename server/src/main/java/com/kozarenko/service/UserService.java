package com.kozarenko.service;

import com.kozarenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}
