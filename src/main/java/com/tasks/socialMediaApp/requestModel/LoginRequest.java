package com.tasks.socialMediaApp.requestModel;

public class LoginRequest {

    String userName;

    String password;

    public LoginRequest() {
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
