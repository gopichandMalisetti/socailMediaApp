package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResponseLike {

    ResponsePost post;
    User user;
    Date likedTime;

    public ResponseLike() {
    }

    public Date getLikedTime() {
        return likedTime;
    }

    public ResponsePost getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public void setLikedTime(Date likedTime) {
        this.likedTime = likedTime;
    }

    public void setPost(ResponsePost post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
