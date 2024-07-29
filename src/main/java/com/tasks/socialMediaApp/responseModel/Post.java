package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.Image;

import java.util.List;

public class Post {

    int id;
    String detials;
    int likesCount;
    List<ResponseImage> responseImages;

    public Post() {
    }

    public String getDetials() {
        return detials;
    }

    public void setDetials(String detials) {
        this.detials = detials;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<ResponseImage> getResponseImages() {
        return responseImages;
    }

    public void setResponseImages(List<ResponseImage> responseImages) {
        this.responseImages = responseImages;
    }
}
