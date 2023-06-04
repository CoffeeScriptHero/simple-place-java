package com.kozarenko.repository;

import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
  List<Comment> findCommentsByPost(Post post);
}
