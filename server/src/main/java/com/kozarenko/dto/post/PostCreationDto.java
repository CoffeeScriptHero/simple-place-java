package com.kozarenko.dto.post;

import lombok.Data;

@Data
public class PostCreationDto {
  private String id;
  private String description;
  private String base64Image;
}
