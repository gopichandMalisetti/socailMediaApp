package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "images")
public class Image implements Serializable {


    int id;
    String url;
    Post post;

    public Image() {
    }

    public Image(int id, Post post, String url) {
        this.id = id;
        this.post = post;
        this.url = url;
    }


    @ManyToOne
    @JoinColumn(name = "post_id")
    public Post getPost() {
        return post;
    }

    public String getUrl() {
        return url;
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
