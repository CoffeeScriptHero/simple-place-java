package com.kozarenko.model.base;

import com.kozarenko.model.additional.Like;

import javax.persistence.*;
import java.util.List;

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

  //  @OneToMany(mappedBy = "author")
  //  private List<Post> posts;

  //  @OneToMany(mappedBy = "likedBy")
  //  private List<Like> likes;
}
