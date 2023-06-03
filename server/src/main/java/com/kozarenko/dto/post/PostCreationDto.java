package com.kozarenko.dto.post;

import lombok.Data;

@Data
public class PostCreationDto {

  private String description;

  private String userId;

  private String base64Image;

}
