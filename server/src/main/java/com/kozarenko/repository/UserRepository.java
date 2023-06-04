package com.kozarenko.repository;

import com.kozarenko.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

  @Query(value = "SELECT * FROM users WHERE username regexp ?1", nativeQuery = true)
  List<User> findUsersByUsernameMatchesRegex(String regex);

  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
