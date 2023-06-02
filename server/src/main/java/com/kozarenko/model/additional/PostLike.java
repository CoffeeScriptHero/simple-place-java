package com.kozarenko.model.additional;

import com.kozarenko.model.additional.keys.PostLikePk;
import com.kozarenko.model.base.Post;
import com.kozarenko.model.base.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post_likes")
public class PostLike {

    @EmbeddedId
    private PostLikePk postLikePk;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User likedBy;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @MapsId("postId")
    private Post likedPost;

    public PostLike() {}

    public PostLike(User likedBy, Post likedPost) {
        this.postLikePk = new PostLikePk(likedBy.getId(), likedPost.getId());
        this.likedBy = likedBy;
        this.likedPost = likedPost;
    }
}
