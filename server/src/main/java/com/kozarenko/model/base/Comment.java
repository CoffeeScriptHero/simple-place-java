package com.kozarenko.model.base;

import com.kozarenko.model.additional.CommentLike;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

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

  public Comment() {}

  public Comment(String id) {
    this.id = id;
  }
}
