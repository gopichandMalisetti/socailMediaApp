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
    List<Follow> followingUsers = new ArrayList<>();
    List<Follow> followedUsers = new ArrayList<>();
    List<Post> posts = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();
    List<Like> likes = new ArrayList<>();

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Comment> getComments() {
        return comments;
    }

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Follow> getFollowedUsers() {
        return followedUsers;
    }

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Follow> getFollowingUsers() {
        return followingUsers;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFollowingUsers(List<Follow> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public void setFollowedUsers(List<Follow> followedUsers) {
        this.followedUsers = followedUsers;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Like> getLikes() {
        return likes;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Post> getPosts() {
        return posts;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
