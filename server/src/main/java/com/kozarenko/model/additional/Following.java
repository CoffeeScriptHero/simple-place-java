package com.kozarenko.model.additional;

import com.kozarenko.model.additional.keys.FollowingPk;
import com.kozarenko.model.base.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "followings")
public class Following {

  @EmbeddedId
  private FollowingPk followingPk;

  @ManyToOne
  @JoinColumn(name = "follower_id")
  @MapsId("followerId")
  private User followerUser;

  @ManyToOne
  @JoinColumn(name = "followed_id")
  @MapsId("followedId")
  private User followedUser;

  public Following() {}

  public Following(User followerUser, User followedUser) {
    this.followingPk = new FollowingPk(followerUser.getId(), followedUser.getId());
    this.followerUser = followerUser;
    this.followedUser = followedUser;
  }
}
