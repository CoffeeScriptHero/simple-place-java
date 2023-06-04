package com.kozarenko.model.base;

import com.kozarenko.model.additional.Following;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "profile_img_url")
  private String profileImgUrl;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Following> followers;

  @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Following> followings;

  public User() {}

  public User(String id) {
    this.id = id;
  }
}
