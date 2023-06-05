package com.kozarenko.service;

import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.model.base.Post;
import com.kozarenko.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  public Post getReferenceById(String id) throws NoPostWithSuchIdException {
    if (!postRepository.existsById(id)) {
      throw new NoPostWithSuchIdException();
    }
    return postRepository.getReferenceById(id);
  }

  public List<Post> getPosts(int page, int postsPerPage) {
    return postRepository.findAll(PageRequest.of(page, postsPerPage, Sort.by( Sort.Direction.DESC,"createdDate"))).toList();
  }

  public List<Post> getPostsByUsername(String username) {
    return postRepository.findPostsByAuthorUsername(username);
  }

  public Post save(Post post) {
    post.setCreatedDate(LocalDateTime.now());
    return postRepository.save(post);
  }

  public boolean existsById(String id) {
    return postRepository.existsById(id);
  }

  public void delete(Post post) {
    postRepository.delete(post);
  }
}
