package com.tasks.socialMediaApp.responseModel;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponsePost {

    int id;
    String detials;
    int likesCount;
    List<ResponseImage> responseImages;

    public ResponsePost() {
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
