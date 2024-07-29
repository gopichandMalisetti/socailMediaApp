package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;

import java.util.Date;

public class ResponseComment {

    Integer id;

    String description;

    Date createdTime;

    User user;

    Post post;

    public ResponseComment() {
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }
}
