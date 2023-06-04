package com.kozarenko.repository;

import com.kozarenko.model.additional.PostLike;
import com.kozarenko.model.additional.keys.PostLikePk;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePk> {
  int countAllByLikedPost(Post post);

  @Query("SELECT pl.likedBy FROM PostLike pl WHERE pl.likedPost.id = ?1")
  List<User> findUsersByLikedPost(String id);

  @Transactional
  void deleteByLikedByAndLikedPost(User user, Post post);
}
