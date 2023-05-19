package com.kozarenko.service;

import com.kozarenko.model.base.User;
import com.kozarenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public void save(User user) {
    userRepository.save(user);
  }
}
