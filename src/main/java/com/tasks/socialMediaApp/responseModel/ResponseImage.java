package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.Post;
import org.springframework.stereotype.Component;

@Component
public class ResponseImage {

    int id;
    String url;
    Post post;

    public ResponseImage() {
    }

    public int getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public String getUrl() {
        return url;
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
