package com.kozarenko.repository;

import com.kozarenko.model.additional.CommentLike;
import com.kozarenko.model.additional.keys.CommentLikePk;
import com.kozarenko.model.base.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikePk> {
  int countAllByLikedComment(Comment comment);
}
