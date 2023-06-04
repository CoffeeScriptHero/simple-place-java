package com.kozarenko.repository;

import com.kozarenko.model.base.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
  List<Post> findPostsByAuthorUsername(String username);
}
