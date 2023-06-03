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
public class FollowingPk implements Serializable {

  @Column(name = "follower_id")
  private String followerId;

  @Column(name = "followed_id")
  private String followedId;
}
