package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class UserVo extends BaseInfoVo {

    @SerializedName("UserID")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
