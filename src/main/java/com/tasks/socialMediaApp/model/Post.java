package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post implements Serializable {

    int id;
    String details;
    int likesCount;
    User user;

    public void setId(int id) {
        this.id = id;
    }

    public Post() {
    }

    public Post(String details, User user) {
        this.details = details;
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    @Column(name = "likesCount")
    public int getLikesCount() {
        return likesCount;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

}
