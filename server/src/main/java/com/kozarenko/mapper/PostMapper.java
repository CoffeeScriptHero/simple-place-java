package com.kozarenko.mapper;

import com.kozarenko.dto.post.PostCreationDto;
import com.kozarenko.dto.post.PostInfoDto;
import com.kozarenko.model.base.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
  private final ModelMapper modelMapper;

  public PostMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
    TypeMap<PostCreationDto, Post> propertyMapper = modelMapper.createTypeMap(PostCreationDto.class, Post.class);
    propertyMapper.addMappings(mapper -> mapper.skip(Post::setImageUrl));
  }

  public Post mapToPost(PostCreationDto postDto) {
    return modelMapper.map(postDto, Post.class);
  }

  public PostInfoDto mapToPostInfoDto(Post post) {
    return modelMapper.map(post, PostInfoDto.class);
  }
}
