package com.kozarenko.model.additional;

import com.kozarenko.model.base.Comment;
import com.kozarenko.model.additional.keys.CommentLikePk;
import com.kozarenko.model.base.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment_likes")
public class CommentLike {

  @EmbeddedId
  private CommentLikePk commentLikePk;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @MapsId("userId")
  private User likedBy;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  @MapsId("commentId")
  private Comment likedComment;

  public CommentLike() {}

  public CommentLike(User likedBy, Comment likedComment) {
    this.commentLikePk = new CommentLikePk(likedBy.getId(), likedComment.getId());
    this.likedBy = likedBy;
    this.likedComment = likedComment;
  }
}
