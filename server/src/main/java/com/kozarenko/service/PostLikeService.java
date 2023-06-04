package com.kozarenko.service;

import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.exception.custom.NoUserWithSuchUsernameException;
import com.kozarenko.model.additional.PostLike;
import com.kozarenko.model.additional.keys.PostLikePk;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import com.kozarenko.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostService postService;
  private final UserService userService;

  public boolean setLike(String userId, String postId) {
    if (!existsByIds(userId, postId)) {
      save(userId, postId);
      return true;
    } else {
      delete(userId, postId);
      return false;
    }
  }

  public void save(String userId, String postId) {
    postLikeRepository.save(new PostLike(new User(userId), new Post(postId)));
  }

  public void delete(String userId, String postId) {
    postLikeRepository.deleteByLikedByAndLikedPost(new User(userId), new Post(postId));
  }

  public List<User> getUsersListOfLikes(String id)
          throws NoPostWithSuchIdException {
    if (!postService.existsById(id)) {
      throw new NoPostWithSuchIdException();
    }
    return postLikeRepository.findUsersByLikedPost(id);
  }

  public int countPostLikes(String id) {
    return postLikeRepository.countAllByLikedPost(new Post(id));
  }

  public boolean existsByIds(String userId, String postId) {
    return postLikeRepository.existsById(new PostLikePk(userId, postId));
  }
}
