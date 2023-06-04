package com.kozarenko.service;

import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.model.base.Comment;
import com.kozarenko.model.base.Post;
import com.kozarenko.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostService postService;

  public List<Comment> getComments(String postId) throws NoPostWithSuchIdException {
    Post post = postService.getReferenceById(postId);
    return commentRepository.findCommentsByPost(post);
  }
}
