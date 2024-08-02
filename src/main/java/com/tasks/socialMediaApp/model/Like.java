package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
public class Like implements Serializable {
    User user;
    Post post;
    Date likedTime;

    public Like() {
    }

    public Like(Date likedTime, Post post, User user) {
        this.likedTime = likedTime;
        this.post = post;
        this.user = user;
    }

    @Column(name = "liked_time")
    public Date getLikedTime() {
        return likedTime;
    }

    @ManyToOne
    @Id
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }


    @ManyToOne
    @Id
    @JoinColumn(name = "post_id")
    public Post getPost() {
        return post;
    }

    public void setLikedTime(Date likedTime) {
        this.likedTime = likedTime;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
