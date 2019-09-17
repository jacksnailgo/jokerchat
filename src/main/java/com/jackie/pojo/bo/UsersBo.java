package com.jackie.pojo.bo;

import javax.persistence.Column;
import javax.persistence.Id;

public class UsersBo {
    private String userId;

    private String userData;

    public String getUserId() {
        return userId;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}