package com.kozarenko.model.additional.keys;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LikePk implements Serializable {

  private Long userId;

  private Long postId;
}
