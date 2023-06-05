package com.kozarenko.service;

import com.kozarenko.exception.custom.NoCommentWithSuchIdException;
import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.model.additional.CommentLike;
import com.kozarenko.model.additional.keys.CommentLikePk;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.User;
import com.kozarenko.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentService commentService;

  public boolean saveLike(String userId, String commentId) {
    if (!existsByIds(userId, commentId)) {
      save(userId, commentId);
      return true;
    } else {
      delete(userId, commentId);
      return false;
    }
  }

  public void save(String userId, String commentId) {
    commentLikeRepository.save(new CommentLike(new User(userId), new Comment(commentId)));
  }

  public void delete(String userId, String commentId) {
    commentLikeRepository.deleteByLikedByAndLikedComment(new User(userId), new Comment(commentId));
  }

  public List<User> getUsersListOfLikes(String id) throws NoCommentWithSuchIdException {
    if (!commentService.existsById(id)) {
      throw new NoCommentWithSuchIdException();
    }
    return commentLikeRepository.findUsersByLikedComment(id);
  }


  public int countCommentLikes(String id) {
    return commentLikeRepository.countAllByLikedComment(new Comment(id));
  }

  public boolean existsByIds(String userId, String commentId) {
    return commentLikeRepository.existsById(new CommentLikePk(userId, commentId));
  }
}
