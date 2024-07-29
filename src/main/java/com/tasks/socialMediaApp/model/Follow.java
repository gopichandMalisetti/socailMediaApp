package com.tasks.socialMediaApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "follows")
public class Follow implements Serializable {

    FollowId followId;
    User followedUser;
    User followingUser;
    Date followedTime;

    public Follow() {
    }

    public Follow(Date followedTime, FollowId followId) {
        this.followedTime = followedTime;
        this.followId = followId;
    }

    @EmbeddedId
    public FollowId getFollowId() {
        return followId;
    }


    public void setFollowedTime(Date followedTime) {
        this.followedTime = followedTime;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public void setFollowId(FollowId followId) {
        this.followId = followId;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }


    @ManyToOne
    @MapsId("followedUser")
    @JoinColumn(name = "followedUserId")
    public User getFollowedUser() {
        return followedUser;
    }

    @ManyToOne
    @MapsId("followingUser")
    @JoinColumn(name = "followingUserId")
    public User getFollowingUser() {
        return followingUser;
    }

    @Column(name = "createdTime")
    public Date getFollowedTime() {
        return followedTime;
    }

}
