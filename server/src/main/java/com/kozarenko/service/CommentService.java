package com.kozarenko.service;

import com.kozarenko.exception.custom.AccessDeniedException;
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

  public boolean existsById(String id) {
    return commentRepository.existsById(id);
  }

  public List<Comment> save(Comment comment) throws NoPostWithSuchIdException {
    commentRepository.save(comment);
    return getComments(comment.getPost().getId());
  }

  public List<Comment> delete(String id, String username) throws AccessDeniedException, NoPostWithSuchIdException {
    Comment comment = commentRepository.getReferenceById(id);
    Post post = postService.getReferenceById(comment.getPost().getId());

    if (!post.getAuthor().getUsername().equals(username) && !comment.getUser().getUsername().equals(username)) {
      throw new AccessDeniedException();
    }

    commentRepository.deleteById(id);
    return getComments(post.getId());
  }

  public List<Comment> getComments(String postId) throws NoPostWithSuchIdException {
    Post post = postService.getReferenceById(postId);
    return commentRepository.findCommentsByPost(post);
  }
}
