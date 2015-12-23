package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class MessageVo extends BaseInfoVo {

    @SerializedName("VerifyCode")
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
