package com.tasks.socialMediaApp.model;

import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;


public class LikeId implements Serializable {

    User user;

    Post post;

    public LikeId() {
    }

    public LikeId(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(user, likeId.user) && Objects.equals(post, likeId.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public Post getPost() {
        return post;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

}
