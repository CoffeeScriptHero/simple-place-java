package com.kozarenko.model.base;

import com.kozarenko.model.additional.PostLike;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "description", length = 350)
  private String description;

  @Column(name = "image_url")
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User author;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @OneToMany(mappedBy = "likedPost", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> likes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  public Post() {}

  public Post(String id) {
    this.id = id;
  }
}
