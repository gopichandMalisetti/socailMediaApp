package com.tasks.socialMediaApp.responseModel;

public class LoginResponse {

    String userName;
    String jwtToken;
    String refreshToken;

    public LoginResponse() {
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
