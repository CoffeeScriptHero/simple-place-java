package com.kozarenko.repository;

import com.kozarenko.model.base.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}
