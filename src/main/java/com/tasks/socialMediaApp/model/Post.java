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
    List<Comment> comments = new ArrayList<>();
    List<Like> likes = new ArrayList<>();
    List<Image> Images = new ArrayList<>();


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && likesCount == post.likesCount && Objects.equals(details, post.details) && Objects.equals(user, post.user) && Objects.equals(comments, post.comments) && Objects.equals(likes, post.likes) && Objects.equals(Images, post.Images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, likesCount, user, comments, likes, Images);
    }

    public Post() {
    }

    public Post(String details, int id, User user) {
        this.details = details;
        this.id = id;
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImages(List<Image> images) {
        Images = images;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Comment> getComments() {
        return comments;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Image> getImages() {
        return Images;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Like> getLikes() {
        return likes;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }





}
