package com.tasks.socialMediaApp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowId implements Serializable{

    // me following someone
    User followedUser;

    // someone following me
    User followingUser;

    public FollowId() {
    }

    public FollowId(User followedUser, User followingUser) {
        this.followedUser = followedUser;
        this.followingUser = followingUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(followingUser, followId.followingUser) && Objects.equals(followedUser, followId.followedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followingUser, followedUser);
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    @ManyToOne
    public User getFollowedUser() {
        return followedUser;
    }

    @ManyToOne
    public User getFollowingUser() {
        return followingUser;
    }

}
