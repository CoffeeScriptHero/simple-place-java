package com.kozarenko.model.base;

import com.kozarenko.model.additional.Following;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "profileImg")
  private String profileImg;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Following> followers;

  @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Following> followings;
}
