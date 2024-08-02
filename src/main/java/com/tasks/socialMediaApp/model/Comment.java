package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {


    Integer id;

    String description;

    Date createdTime;

    User user;

    Post post;

    public Comment() {
    }

    public Comment(Date createdTime, String description, Integer id, Post post, User user) {
        this.createdTime = createdTime;
        this.description = description;
        this.id = id;
        this.post = post;
        this.user = user;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "post_id")
    public Post getPost() {
        return post;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
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

    @Column(name = "createdTime")
    public Date getCreatedTime() {
        return createdTime;
    }

    @Column(name = "comment_description")
    public String getDescription() {
        return description;
    }

}
