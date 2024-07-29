package com.tasks.socialMediaApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Profile {

    String fullName;
    String bio;
    String photoUrl;
    String phno;
    String emailId;

    @Column(name = "emailId")
    public String getEmailId() {
        return emailId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Column(name = "phno")
    public String getPhno() {
        return phno;
    }

    @Column(name = "fullName")
    public String getFullName() {
        return fullName;
    }

    @Column(name = "user_photo_url")
    public String getPhotoUrl() {
        return photoUrl;
    }

    public Profile() {
    }

    @Column(name = "bio")
    public String getBio() {
        return bio;
    }

    public Profile(String fullName, String phno, String photoUrl, String bio, String emailId) {
        this.fullName = fullName;
        this.phno = phno;
        this.photoUrl = photoUrl;
        this.bio = bio;
        this.emailId = emailId;
    }
}
