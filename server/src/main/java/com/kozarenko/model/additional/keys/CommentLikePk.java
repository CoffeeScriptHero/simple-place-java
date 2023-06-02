package com.kozarenko.model.additional.keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikePk implements Serializable {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "comment_id")
  private Long commentId;
}
