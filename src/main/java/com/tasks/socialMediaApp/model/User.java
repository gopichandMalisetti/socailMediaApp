package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private Integer id;
    private String password;
    private String userName;
    private Profile profile;
    private RefreshToken refreshToken;

    public User() {
    }

    public User(int id, Profile profile, String userName, String password) {
        this.id = id;
        this.profile = profile;
        this.userName = userName;
        this.password = password;
    }

    @Column(name = "user_password")
    public String getPassword() {
        return password;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }


    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
