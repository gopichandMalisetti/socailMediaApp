package com.tasks.socialMediaApp.requestModel;

import com.tasks.socialMediaApp.model.Image;

import java.util.List;

public class RequestPost {

    String details;
    List<RequestImage> images;

    public String getDetails() {
        return details;
    }

    public List<RequestImage> getImages() {
        return images;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImages(List<RequestImage> images) {
        this.images = images;
    }

}
