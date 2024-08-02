package com.tasks.socialMediaApp.responseModel;

import com.tasks.socialMediaApp.model.FollowId;
import com.tasks.socialMediaApp.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResponseFollow {

    String userName;
    String followedUserName;
    Date followedTime;

    public ResponseFollow() {
    }

    public Date getFollowedTime() {
        return followedTime;
    }

    public String getFollowedUserName() {
        return followedUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setFollowedTime(Date followedTime) {
        this.followedTime = followedTime;
    }

    public void setFollowedUserName(String followedUserName) {
        this.followedUserName = followedUserName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
