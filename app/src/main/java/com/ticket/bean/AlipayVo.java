package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class AlipayVo extends BaseInfoVo {

    @SerializedName("SignedContent")
    private String signedContent;

    public String getSignedContent() {
        return signedContent;
    }

    public void setSignedContent(String signedContent) {
        this.signedContent = signedContent;
    }
}
