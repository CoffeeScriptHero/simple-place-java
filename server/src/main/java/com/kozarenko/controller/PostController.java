package com.kozarenko.controller;

import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.model.base.Post;
import com.kozarenko.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final CloudinaryService cloudinaryService;

  //  @PostMapping()
  //  public ResponseEntity<?> createPost(@RequestBody PostCreationDto postDto) {
  //
  //
  //
  //    Map<String, Object> uploadResult = cloudinaryService.uploadPostPic(postDto.getBase64Image());
  //
  //
  //
  //  }


}
