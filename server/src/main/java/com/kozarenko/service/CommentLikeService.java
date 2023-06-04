package com.kozarenko.service;

import com.kozarenko.model.additional.keys.CommentLikePk;
import com.kozarenko.model.base.Comment;
import com.kozarenko.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;

  public int countCommentLikes(String id) {
    return commentLikeRepository.countAllByLikedComment(new Comment(id));
  }

  public boolean existsByIds(String userId, String commentId) {
    return commentLikeRepository.existsById(new CommentLikePk(userId, commentId));
  }
}
