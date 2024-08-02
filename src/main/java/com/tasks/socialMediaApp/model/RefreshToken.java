package com.tasks.socialMediaApp.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@Embeddable
public class RefreshToken {

    String refreshToken;
    Date expiryTime;
    public Date getExpiryTime() {
        return expiryTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
