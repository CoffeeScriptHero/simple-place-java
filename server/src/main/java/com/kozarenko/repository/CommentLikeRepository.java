package com.kozarenko.repository;

import com.kozarenko.model.additional.CommentLike;
import com.kozarenko.model.additional.keys.CommentLikePk;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikePk> {
  int countAllByLikedComment(Comment comment);

  @Query("SELECT cl.likedBy FROM CommentLike cl WHERE cl.likedComment.id = ?1")
  List<User> findUsersByLikedComment(String id);

  @Transactional
  void deleteByLikedByAndLikedComment(User user, Comment comment);
}
