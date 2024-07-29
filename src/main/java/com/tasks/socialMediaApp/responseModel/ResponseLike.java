package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.User;

import java.util.Date;

public class ResponseLike {

    Post post;
    User user;
    Date likedTime;

    public ResponseLike() {
    }

    public Date getLikedTime() {
        return likedTime;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
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
