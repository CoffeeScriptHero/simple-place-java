package com.kozarenko.service;

import com.kozarenko.exception.custom.NoPostWithSuchIdException;
import com.kozarenko.model.base.Post;
import com.kozarenko.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

  public Post save(Post post) {
    return postRepository.save(post);
  }

  public void delete(Post post) {
    postRepository.delete(post);
  }
}
