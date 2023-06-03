package com.kozarenko.model.base;

import com.kozarenko.model.additional.PostLike;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description", length = 350)
  private String description;

  @Column(name = "image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToMany(mappedBy = "likedPost", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> likes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;
}
