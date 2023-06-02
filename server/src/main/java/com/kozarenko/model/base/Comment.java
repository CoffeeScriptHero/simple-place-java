package com.kozarenko.model.base;

import com.kozarenko.model.additional.CommentLike;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(name = "text")
  private String text;

  @OneToMany(mappedBy = "likedComment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CommentLike> likes;
}
