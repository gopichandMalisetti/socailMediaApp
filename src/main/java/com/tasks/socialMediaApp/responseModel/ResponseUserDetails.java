package com.tasks.socialMediaApp.responseModel;


public class ResponseUserDetails {

    private String userName;
    private String fullName;
    private String bio;
    private String photoUrl;

    public ResponseUserDetails() {
    }

    public String getBio() {
        return bio;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
