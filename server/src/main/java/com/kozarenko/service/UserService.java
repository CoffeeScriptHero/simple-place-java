package com.kozarenko.service;

import com.kozarenko.exception.custom.NoUserWithSuchIdException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.exception.custom.UsernameTakenException;
import com.kozarenko.model.base.User;
import com.kozarenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public void throwExceptionIfNoUserWithUsername(String username) throws NoUserWithSuchUsernameException {
    if (!userRepository.existsByUsername(username)) {
      throw new NoUserWithSuchUsernameException();
    }
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public void checkUsernameTaken(String username) throws UsernameTakenException {
    if (userRepository.existsByUsername(username)) {
      throw new UsernameTakenException();
    }
  }

  public User findByUsername(String username) throws NoUserWithSuchUsernameException {
    return userRepository.findByUsername(username).orElseThrow(NoUserWithSuchUsernameException::new);
  }

  public User findById(String id) throws NoUserWithSuchIdException {
    return userRepository.findById(id).orElseThrow(NoUserWithSuchIdException::new);
  }

  public List<User> findByRegex(String regex) {
    return userRepository.findUsersByUsernameMatchesRegex(regex);
  }

  public User getReferenceById(String id) {
    return userRepository.getReferenceById(id);
  }

  public void save(User user) {
    userRepository.save(user);
  }
}
